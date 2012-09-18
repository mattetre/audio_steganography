package edu.neu.ccs.kemf.itest;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.junit.Test;

import edu.neu.ccs.kemf.DataUtil;
import edu.neu.ccs.kemf.Steganographer;
import edu.neu.ccs.kemf.SystemUtil;
import edu.neu.ccs.kemf.algorithms.AvailableAlgorithms;

public class ITestRun {

	private static final String ITEST_DIR = "test/resources/itest";
	private static final String CARRIER_DIR = ITEST_DIR + "/audio";
	private static final String MESSAGE_DIR = ITEST_DIR + "/message";
	private static final String OUTPUT_DIR = "/output";

	@Test
	public void testStegan() {
		File[] carrierFiles = getAllDirectoryFiles(CARRIER_DIR);
		File[] messageFiles = getAllDirectoryFiles(MESSAGE_DIR);
		AvailableAlgorithms[] algorithms = AvailableAlgorithms.values();
		
		// loop through all message files and encode to each carrier
		for (File message : messageFiles) {
			for (File carrier : carrierFiles) {
				if (carrier.exists() && message.exists()) {
					System.out.println("Carrier: " + carrier.getName());
					System.out.println("Message: " + message.getName());
					
					for (AvailableAlgorithms algorithm : algorithms) {
						System.out.println("\t" + algorithm.toString());
						Double percentDiff = runStegan(carrier, message, algorithm);
						if (percentDiff == null) {
							System.out.println("\tMessage too large");
							continue;
						}
						
						System.out.println("\tPercent Bit Difference: " + percentDiff);
						// assert less than 10% data loss
						assertTrue(percentDiff < 10);
					}
				}
			}
		}
	}
	
	
	private Double runStegan(File inputFile, File messageFile, AvailableAlgorithms algorithm) {
		try {
			// create output dir
			File itestDir = getRelativeFile(ITEST_DIR);
			File output = new File(itestDir, OUTPUT_DIR + File.separator + algorithm.toString());
			output.mkdirs();
			
			// set output files
			String fileName = inputFile.getName();
			String outputFileName = fileName.substring(0, fileName.lastIndexOf("."));
			String outputFileExt = fileName.substring(fileName.lastIndexOf("."));
			outputFileName = outputFileName + "_OUTPUT" + outputFileExt;
			String outputMessageName = outputFileName + "_MESSAGE_" + messageFile.getName();

			File outputAudioFile = new File(output, outputFileName);
			File outputMessageFile = new File(output, outputMessageName);
			
			// setup encoder
			Steganographer steganEnc = Steganographer.createEncodingSteganographer(inputFile, outputAudioFile, messageFile, algorithm);
			boolean success = steganEnc.encode();
			if (success) {
				// setup decoder
				Steganographer steganDec = Steganographer.createDecodingSteganographer(outputAudioFile, outputMessageFile, algorithm);
				success = steganDec.decode();
			}
			
			if (success)
				return getPercentDiff(messageFile, outputMessageFile);
			
			
		} catch (Exception e) {
			System.err.println(algorithm.toString());
			e.printStackTrace();
		}
		return null;
	}

	private File getRelativeFile(String filePath) {
		String dir = System.getProperty("user.dir") + File.separator + "target" + File.separator;
		return new File(dir + filePath);
	}
	
	private File[] getAllDirectoryFiles(String dirPath) {
		File dir = getRelativeFile(dirPath);
		if (dir.isDirectory()) {
			return dir.listFiles();
		}

		return null;
	}
	
	private Double getPercentDiff(File original, File newFile) {
		
		List<BitSet> originalBitSets = getBitSets(original);
		List<BitSet> newBitSets = getBitSets(newFile);
		int originalByteSize = originalBitSets.size();
		int newByteSize = newBitSets.size();
		
		int loopLength = Math.min(originalByteSize, newByteSize);
		
		int differences = 0;
		for (int i = 0; i < loopLength; i++ ) {
			BitSet orgBitSet = originalBitSets.get(i);
			BitSet newBitSet = newBitSets.get(i);
			
			newBitSet.xor(orgBitSet);
			differences += newBitSet.cardinality();				
		}
		
		differences += Math.abs(originalByteSize - newByteSize);
		
		
		int originalTotalBitNumber = originalByteSize * Byte.SIZE;
		return ((double)differences) / ((double)originalTotalBitNumber) * 100.0;

		 
	}
	
	private List<BitSet> getBitSets(File file) {
		List<BitSet> bitSets = new ArrayList<BitSet>(0);
		
		byte[] buff = new byte[(int) file.length()];
		try {
			InputStream instream = new FileInputStream(file);
			instream.read(buff);
			for (int i = 0; i < buff.length; i++) {
				byte inByte = buff[i];
				bitSets.add(DataUtil.byte8ToBitSet(inByte));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitSets;
	}
	
	
	
}
