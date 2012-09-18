package edu.neu.ccs.kemf;

import java.io.IOException;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;

/**
 * Class for wave file functionality
 */
public class WaveFileUtil {
	
	// the max sample size for a 16 bit sample
	public static final double MAX_16_SAMPLE = 32768.0;
	// the header size for a wave file
	public static final int HEADER_SIZE = 44;

	/**
	 * Returns the left channel samples read from the array of left and right samples<br>
	 * <b> *Assumes first is left channel* </b>
	 * 
	 * @param fullSamples The int array of left and right samples
	 * @param wavFormat The wave file format corresponding to the data
	 * @return int array of samples
	 */
	public static int[] extractLeftSamples(int[] fullSamples, AudioFormat wavFormat) throws IOException {
		return extractSamples(fullSamples, wavFormat, 0);
	}
	
	/**
	 * Returns the right channel samples read from the array of left and right samples<br>
	 * <b> *Assumes first is left channel* </b>
	 * 
	 * @param fullSamples The int array of left and right samples
	 * @param wavFormat The wave file format corresponding to the data
	 * @return int array of samples
	 */
	public static int[] extractRightSamples(int[] fullSamples, AudioFormat wavFormat) throws IOException {
		return extractSamples(fullSamples, wavFormat, 1);
	}
	
	/**
	 * Returns the the samples extracted from the 2 channel samples based on starting position<br>
	 * 
	 * @param fullSamples The int array of left and right samples
	 * @param wavFormat The wave file format corresponding to the data
	 * @param startPos The position in the array to start reading from
	 * @return int array of samples
	 */
	private static int[] extractSamples(int[] fullSamples, AudioFormat wavFormat, int startPos) throws IOException {
		// check channels and throw exception
		check2Channels(wavFormat);
		
		int[] rightSamples = new int[fullSamples.length / 2];
		int fullSamplesPosition = startPos;
		for (int i = 0; i < rightSamples.length; i ++) {
			rightSamples[i] = fullSamples[fullSamplesPosition];
			fullSamplesPosition += 2;
		}
		
		return rightSamples;
	}
	
	/**
	 * Returns the right channel samples read from the raw WAVE byte array<br>
	 * <b> Assumes first is left channel </b>
	 * 
	 * @param byteArray The byte array to read from
	 * @param wavFormat The wave file format corresponding to the data
	 * @return int array of samples
	 */
	public static int[] extractRightSamples(byte[] byteArray, AudioFormat wavFormat) {
		if (wavFormat.getChannels() == 2) {
			// remove first left channel
			byte[] byteArrayCopy = Arrays.copyOfRange(byteArray, 2, byteArray.length);
			return extractSamples(byteArrayCopy, wavFormat.getFrameSize());
		}
		return extractSamples(byteArray, wavFormat);
	}
	
	/**
	 * Returns the left channels samples read from the raw WAVE byte array <br>
	 * <b> NOTE: assumes that array is starting on left sample </b>
	 * 
	 * @param byteArray The byte array to read from
	 * @param wavFormat The wave file format corresponding to the data
	 * @return int array of samples
	 */
	public static int[] extractLeftSamples(byte[] byteArray, AudioFormat wavFormat) {
		if (wavFormat.getChannels() == 2) {
			return extractSamples(byteArray, wavFormat.getFrameSize());
		}
		return extractSamples(byteArray, wavFormat);
	}
	
	/**
	 * Returns an array of all the samples in the raw WAVE byte array
	 * <p>
	 * If the Wave file has 2 two channels both are returned
	 * <br>
	 * For example:
	 * <pre>
	 *   array[0] = sample 1 - left channel
	 *   array[1] = sample 1 - right channel
	 *   array[2] = sample 2 - left channel
	 *   array[3] = sample 2 - right channel
	 *   ...   
	 *   </pre>
	 * </p> 
	 * @param byteArray The byte array to read from
	 * @param wavFormat The wave file format corresponding to the data
	 * @return int array of samples
	 */
	public static int[] extractSamples(byte[] byteArray, AudioFormat wavFormat) {
		return extractSamples(byteArray, (wavFormat.getSampleSizeInBits() / Byte.SIZE));
	}
	
	
	/**
 	 * Returns an array of all the samples in the raw WAVE byte array
 	 * 
	 * @param byteArray the byte array to read the samples from
	 * @param inc the number of bytes to increment by for each sample
	 * @return int array of samples
	 */
	public static int[] extractSamples(byte[] byteArray, int inc) {
		int samplesLength = byteArray.length / inc;
		
		int[] returnSamples = new int[samplesLength];
		int readPos = 0;
		// loop through the samples
		for (int i = 0; i < samplesLength; i++) {
			int sample = 0;

			// convert read bytes to little endian sample
			// maybe change this later
			sample = ((int) ((byteArray[readPos] & 0xFF) | 
						     (byteArray[readPos + 1] << 8)));
			
			returnSamples[i] = sample;
			
			// increment the position to read
			readPos += inc;
		}
		
		return returnSamples;
	}
	
	/**
	 * Scale all the samples by the max 16 bit sample
	 * 
	 * @return Double array with samples
	 */
	public static double[] scaleSamples(int[] intSamples) {
		int n = intSamples.length;
		double[] returnSamples = new double[n];
		// loop through the samples
		for (int i = 0; i < n; i++) {
			returnSamples[i] = (double)(intSamples[i]) / MAX_16_SAMPLE;
		}
		return returnSamples;
	}
	
	/**
	 * Unscale all the samples by the max 16 bit sample
	 * 
	 * @return Int array with samples
	 */
	public static int[] unscaleSamples(double[] intSamples) {
		int n = intSamples.length;
		int[] returnSamples = new int[n];
		// loop through the samples
		for (int i = 0; i < n; i++) {
			returnSamples[i] = (int)((intSamples[i]) * MAX_16_SAMPLE);
		}
		return returnSamples;
	}
	
	/**
	 * Get the index of the complex number with maximum magnitude
	 * 
	 * @param fftArray Array of complex numbers
	 * @return index of max magnitude
	 */
	public static int getMaxMagnitudeIndex(Complex[] fftArray) {
		int maxIndex = -1;
		double maxValue = -Integer.MAX_VALUE;
		
		for (int i = 1; i < fftArray.length; i++) {
			if (fftArray[i].magnitude() > maxValue) {
				maxValue = fftArray[i].magnitude();
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	
	/**
	 * Get all the real values from complex array
	 * 
	 * @param complexArray Complex array
	 * @return double array of complex real values
	 */
	public static double[] complexArrayGetReals(Complex[] complexArray) {
		double[] reals = new double[complexArray.length];
		for (int i = 0; i < complexArray.length; i++) {
			reals[i] = (complexArray[i].getReal());
		}
		return reals;
	}

	/**
	 * Combine left samples and right samples into new array
	 * 
	 * @param leftSamples Unscaled left samples
	 * @param rightSamples Unscaled right samples
	 * @return New unscaled sample with left and right
	 */
	public static int[] combineSamples(int[] leftSamples, int[] rightSamples){
		// TODO: check that sizes are the same
		
		int[] newSamples = new int[leftSamples.length + rightSamples.length];
		
		int newPos = 0;
		for (int i = 0; i < leftSamples.length; i++) {
			newSamples[newPos] = leftSamples[i];
			newSamples[newPos + 1] = rightSamples[i];
			newPos += 2;
		}
		return newSamples;
	}
	
	/**
	 * Get the average magnitude for a complex array
	 * 
	 * @param complexArray An array of complex numbers
	 * @return average magnitude of complex numbers
	 */
	public static double getAverageMag(Complex[] complexArray) {
		int n = complexArray.length;
		double total = 0;
		
		for (int i = 1; i < n; i++) {
			total += complexArray[i].magnitude();
		}
		
		return total / (double)n;
	}
	
	/**
	 * Check if wav file has 2 channels and throw exception
	 * 
	 * @param wavFormat Wave file format
	 * @throws IOException If not 2 channels
	 */
	private static void check2Channels(AudioFormat wavFormat) throws IOException {
		if(wavFormat.getChannels() != 2)
			throw new IOException("Wave file only has 1 channel");
	}

	
	

}
