package edu.neu.ccs.kemf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Class to read wave file input
 */
public class WaveFileInput {

	private File file;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private float sampleRate;
	private byte[] headerData;
	
	private WaveFileInput(File file, AudioInputStream audioStream, byte[] headerData) {
		this.file = file;
		this.audioStream = audioStream; 
		this.audioFormat = audioStream.getFormat();
		this.sampleRate = this.audioFormat.getSampleRate();
		this.headerData = headerData;
	}
	
	/**
	 * Create a new WaveFile object by reading the file path
	 * 
	 * @param wavFile The file path to read
	 * @return New WaveFile object
	 * @throws IOException
	 */
	public static WaveFileInput readWaveFile(File wavFile) throws IOException {
		InputStream audioFileStream = new FileInputStream(wavFile);
		// read the file header
		byte[] headerData = new byte[WaveFileUtil.HEADER_SIZE];
		audioFileStream.read(headerData);
		
		// create the AudioInputStream
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile);
		
			// create the new wave file
			WaveFileInput newWaveFile = new WaveFileInput(wavFile, audioStream, headerData);

			return newWaveFile;
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Read the bytes for a specified number of samples
	 * 
	 * @param samplesToRead The number of samples to read
	 * @return byte array of all the bytes corresponding to the number of samples
	 * @throws IOException
	 */
	private byte[] readSampleBytes(int samplesToRead) throws IOException {
		int numberBytesToRead = samplesToRead * this.audioFormat.getFrameSize();
		return this.readByteData(numberBytesToRead);
	}
	
	/**
	 * Returns unscaled samples
	 * 
	 * @see WaveFileUtil#readSamples(byte[], AudioFormat)
	 * 
	 * @param samplesToRead The number of samples to read
	 * @return an int array of samples
	 * @throws IOException 
	 */
	public int[] readSamplesUnscaled(int samplesToRead) throws IOException {
		byte[] sampleBytes = this.readSampleBytes(samplesToRead);
		return WaveFileUtil.extractSamples(sampleBytes, this.audioFormat);
	}
	
	/**
	 * Returns scaled samples byte a factor of {@value #MAX_16_SAMPLE}
	 * 
	 * @see #readSamplesUnscaled(int)
	 * 
	 * @param samplesToRead The number of samples to read
	 * @return a double array of samples
	 * @throws IOException 
	 */
	public double[] readSamplesScaled(int samplesToRead) throws IOException {
		return WaveFileUtil.scaleSamples(this.readSamplesUnscaled(samplesToRead));
	}
	
	/**
	 * Reads the byte data
	 * 
	 * @param numberOfBytes The number of bytes to read
	 * @return byte array of bytes in audiostream
	 * @throws IOException
	 */
	public byte[] readByteData(int numberOfBytes) throws IOException {
		byte[] readBuffer = new byte[numberOfBytes];
		if (this.audioStream.read(readBuffer) == -1)
			throw new IOException("End of file");
		
		return readBuffer;
	}
	
	/**
	 * Read the remaining bytes leftover from the current audiostream position
	 * @return byte array of remaining bytes in file
	 * @throws IOException
	 */
	public byte[] readRestOfByteData() throws IOException {
		int left = this.audioStream.available();
		return this.readByteData(left);
	}
	
	public File getFile() {
		return this.file;
	}
	
	public AudioInputStream getAudioStream() {
		return this.audioStream;
	}

	public AudioFormat getAudioFormat() {
		return this.audioFormat;
	}
	
	public byte[] getHeaderData() {
		return this.headerData;
	}
	
	public float getSampleRate() {
		return this.sampleRate;
	}
	
	public float getNyquistFrequency() {
		return this.sampleRate / 2;
	}
	
}

