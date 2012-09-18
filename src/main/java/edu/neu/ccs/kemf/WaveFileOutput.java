package edu.neu.ccs.kemf;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Class for creating new wave file 
 */
public class WaveFileOutput {

	private OutputStream outputStream;
	private byte[] waveHeader;
	
	private WaveFileOutput(OutputStream outputStream, byte[] waveHeader) {
		this.outputStream = outputStream;
		this.waveHeader = waveHeader;
	}
	
	/**
	 * Create new Wave File to write
	 * 
	 * @param outputFilePath The path to write the wave file to
	 * @param waveHeader The header of the wav file
	 * @return New WaveFileOutput object
	 * @throws IOException
	 */
	public static WaveFileOutput createNewWaveFile(File outputFilePath, byte[] waveHeader) throws IOException{
		OutputStream outputStream = null;
		try {
			outputStream = new DataOutputStream(new FileOutputStream(outputFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		WaveFileOutput wavFile = new WaveFileOutput(outputStream, waveHeader);
		
		wavFile.writeFileHeader();
		
		return wavFile;
	}
	
	/**
	 * Write samples to file<br> 
	 * <b>Assumes 16-bit samples</b>
	 * <p>
	 * TODO: allow for other sample sizes
	 * </p>
	 * @param samples double array of 16-bit samples to write
	 * @throws IOException 
	 */
	public void writeScaledSamples(double[] samples) throws IOException {
		int n = samples.length;
		// sample size / Byte.SIZE
		// 16 / 8 = 2
		byte[] fullBytes = new byte[n * 2];
		int fullPosition = 0;
		
		for (int i = 0; i < n; i++) {
			int sample = (int)(samples[i] * WaveFileUtil.MAX_16_SAMPLE);
			// convert the double sample to bytes
			byte one = (byte)((int)sample & 0xFF);
			byte two = (byte)(((int)sample >> 8) & 0xFF);
			
			// place in the byte array in little endian format
			fullBytes[fullPosition] = one;
			fullBytes[fullPosition + 1] = two;
			fullPosition += 2;
			
		}
		// write the full byte array to the file
		this.outputStream.write(fullBytes);
	}
	
	/**
	 * Write samples to file<br> 
	 * <b>Assumes 16-bit samples</b>
	 * <p>
	 * TODO: allow for other sample sizes
	 * </p>
	 * @param samples double array of 16-bit samples to write
	 * @throws IOException 
	 */
	public void writeUnscaledSamples(int[] samples) throws IOException {
		int n = samples.length;
		// sample size / Byte.SIZE
		// 16 / 8 = 2
		byte[] fullBytes = new byte[n * 2];
		int fullPosition = 0;
		
		for (int i = 0; i < samples.length; i++) {
			int sample = samples[i];
			// convert the double sample to bytes
			byte one = (byte)((int)sample & 0xFF);
			byte two = (byte)(((int)sample >> 8) & 0xFF);
			
			// place in the byte array in little edian format
			fullBytes[fullPosition] = one;
			fullBytes[fullPosition + 1] = two;
			fullPosition += 2;
			
		}
		// write the full byte array to the file
		this.outputStream.write(fullBytes);
	}
	
	/**
	 * Write the byte array to the file
	 * 
	 * @param byteBuff Byte array to write
	 * @throws IOException
	 */
	public void writeBytes(byte[] byteBuff) throws IOException {
		this.outputStream.write(byteBuff);
	}
	
	/**
	 * Write the file header
	 * @throws IOException
	 */
	private void writeFileHeader() throws IOException {
		this.outputStream.write(this.waveHeader);
	}
	
	/**
	 * Close the wave file
	 * @throws IOException
	 */
	public void closeWaveFile() throws IOException {
		this.outputStream.close();
	}
	
	
}
