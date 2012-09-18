package edu.neu.ccs.kemf.algorithms;

import java.io.File;

import edu.neu.ccs.kemf.WaveFileInput;
import edu.neu.ccs.kemf.WaveFileOutput;

/**
 * Class to represent a Steganography Algorithm
 */
public abstract class StegAlgorithm implements IStegAlgorithm {
	
	private WaveFileInput inputAudioFile;
	private File messageFile;
	private WaveFileOutput outputAudioFile;
	
	/**
	 * Default constructor
	 */
	public StegAlgorithm() { }	
	
	public void setInputAudioFile(WaveFileInput inputFile) {
		this.inputAudioFile = inputFile;
	}
	
	public WaveFileInput getInputAudioFile() {
		return this.inputAudioFile;
	}
	
	public void setOutputAudioFile(WaveFileOutput outputFile) {
		this.outputAudioFile = outputFile;
	}
	
	public WaveFileOutput getOutputAudioFile() {
		return this.outputAudioFile;
	}
	
	public void setMessageFile(File messageFile) {
		this.messageFile = messageFile;
	}
	
	public File getMessageFile() {
		return this.messageFile;
	}
	
	@Override
	public abstract boolean encode();
	
	@Override
	public abstract boolean decode();
	
	
	@Override
	public abstract boolean willItFit(WaveFileInput inputAudioFile, File messageFile);

}
