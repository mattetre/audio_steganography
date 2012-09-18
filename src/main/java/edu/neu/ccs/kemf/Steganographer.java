package edu.neu.ccs.kemf;

import java.io.File;
import java.io.IOException;

import edu.neu.ccs.kemf.algorithms.AvailableAlgorithms;
import edu.neu.ccs.kemf.algorithms.StegAlgorithm;

public class Steganographer {

	private CanonicalFileHandler canonicalFile;
	private StegAlgorithm algorithm;
	private File messageFile;
	
	
	private Steganographer(CanonicalFileHandler canonicalFile, File messageFile, StegAlgorithm algorithm) {
		this.canonicalFile = canonicalFile;
		this.algorithm = algorithm;
		this.messageFile = messageFile;
	}
	
	/**
	 * Create a steganographer class to use for encoding
	 * 
	 * @param inputFile The input audio file to use
	 * @param outputFile The output audio file to create
	 * @param mesageFile The message file to hide
	 * @param algorithm The algorithm to use
	 * @return A new Steganographer object
	 * @throws IOException
	 * @throws Exception
	 */
	public static Steganographer createEncodingSteganographer(File inputFile, File outputFile, File mesageFile, AvailableAlgorithms algorithm) throws IOException, Exception {
		CanonicalFileHandler canonicalFile = CanonicalFileHandler.createEncodingCanonicalFile(inputFile, outputFile);
		return new Steganographer(canonicalFile, mesageFile, algorithm.getAlgorithm());
	}
	
	// TODO: decode algorithm detection
	/**
	 * Create a steganographer class to use for decoding
	 * 
	 * @param inputFile The input audio file to decode from
	 * @param outputFile The output file to write the decoded message to
	 * @param algorithm The algorithm to use (needs to be same used for encoder)
	 * @return A new Steganographer object
	 * @throws IOException
	 * @throws Exception
	 */
	public static Steganographer createDecodingSteganographer(File inputFile, File outputFile, AvailableAlgorithms algorithm) throws IOException, Exception {
		CanonicalFileHandler canonicalFile = CanonicalFileHandler.createDecodingCanonicalFile(inputFile);
		return new Steganographer(canonicalFile, outputFile, algorithm.getAlgorithm());

	}
	
	/**
	 * Begin the encoding process
	 */
	public boolean encode() {
		try {
			
			WaveFileInput inputAudioFile = WaveFileInput.readWaveFile(this.canonicalFile.getCanonicalFile());
			this.algorithm.setInputAudioFile(inputAudioFile);
			this.algorithm.setMessageFile(this.messageFile);
			
			// check the file sizes
			// TODO: throw error
			boolean fits = this.algorithm.willItFit(inputAudioFile, messageFile);
			if (!fits)
				return false;
			
			WaveFileOutput outputAudioFile = WaveFileOutput.createNewWaveFile(this.canonicalFile.encodingFile, inputAudioFile.getHeaderData());
			this.algorithm.setOutputAudioFile(outputAudioFile);
			
			// run encode of alg
			boolean success = this.algorithm.encode();
			if (success) {
				// finish up the ouptfile make sure doesn't need to be converted
				success = this.canonicalFile.completeOutputFile();
				this.canonicalFile.cleanUp();
			}
			
			return success;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * Begin the decoding process
	 */
	public boolean decode() {
		try {
			WaveFileInput inputAudioFile = WaveFileInput.readWaveFile(this.canonicalFile.getCanonicalFile());
			this.algorithm.setInputAudioFile(inputAudioFile);
			
			this.algorithm.setMessageFile(this.messageFile);
			
			// run decode of alg
			this.algorithm.decode();
			this.canonicalFile.cleanUp();

			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
}
