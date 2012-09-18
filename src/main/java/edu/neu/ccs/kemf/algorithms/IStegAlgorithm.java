package edu.neu.ccs.kemf.algorithms;

import java.io.File;

import edu.neu.ccs.kemf.WaveFileInput;

public interface IStegAlgorithm {

	/**
	 * Begin the encoding process
	 */
	boolean encode();
	
	/**
	 * Begin the decoding process
	 */
	boolean decode();
	
	/**
	 * Determine if the message file will fit in the audio file
	 * 
	 * @param inputAudioFile The audio input file
	 * @param messageFile The message file to test size
	 * @return True if it will fit false otherwise
	 */
	boolean willItFit(WaveFileInput inputAudioFile, File messageFile);

	
}
