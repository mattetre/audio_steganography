package edu.neu.ccs.kemf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;

public class FileUtil {
	
	/**
	 * Delete a directory recursively
	 * 
	 * @param fileDir The directory to delete
	 * @return True if delete succeeds false otherwise
	 */
    public static boolean deleteDirectory(File fileDir) {
    	if(fileDir.isDirectory()) {
    		for (File file : fileDir.listFiles()) {
    			if (!file.delete())
    				return false;
    		}
    		return fileDir.delete();
    	}
    	return false;
    }
	
	
	/**
	 * Write the rest of the inputstream to the outputstream
	 * 
	 * @param inputStream The stream to read in
	 * @param outputStream The stream to write to
	 */
	public static void writeRestOfStream(InputStream inputStream, OutputStream outputStream) {
		try {
			// get the number of bytes left
			int inputBytesLeft = inputStream.available();
			byte[] leftOverBuff = new byte[inputBytesLeft];
			// read number of bytes left into leftOverBuff
			inputStream.read(leftOverBuff);
			// write leftOverBuff to outputstream
			outputStream.write(leftOverBuff);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void putBitSet32InStream(BitSet bitSet, InputStream inputStream, OutputStream outputStream) throws IOException {
		putBitSetInStream(32, bitSet, inputStream, outputStream);
	}
	
	public static void putBitSet8InStream(BitSet bitSet, InputStream inputStream, OutputStream outputStream) throws IOException {
		putBitSetInStream(8, bitSet, inputStream, outputStream);
	}
	
	private static void putBitSetInStream(int numberOfBits, BitSet bitSet, InputStream inputStream, OutputStream outputStream) throws IOException {
		
		// loop through bit set (size of a Byte)
		for (int i = (numberOfBits - 1); i >= 0; i--) {
			// read input stream bit
			int inputStreamByte = inputStream.read();

			int newInputStreamByte = DataUtil.setLeastSignificanBit(inputStreamByte, bitSet.get(i));
			
			outputStream.write(newInputStreamByte);
		}
	}
	
	public static void putByteArrayInStream(byte[] byteArray, InputStream inputStream, OutputStream outputStream) throws IOException {
		// loop through the byte array
		for (int i = 0; i < byteArray.length; i++) {
			byte byteInput = byteArray[i];

			BitSet byteInputBitSet = DataUtil.byteToBitSet(byteInput);

			FileUtil.putBitSet8InStream(byteInputBitSet, inputStream, outputStream);
			
		}
	}
}
