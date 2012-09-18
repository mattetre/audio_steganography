package edu.neu.ccs.kemf.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;

import edu.neu.ccs.kemf.Complex;
import edu.neu.ccs.kemf.DataUtil;
import edu.neu.ccs.kemf.Fft;
import edu.neu.ccs.kemf.WaveFileInput;
import edu.neu.ccs.kemf.WaveFileOutput;
import edu.neu.ccs.kemf.WaveFileUtil;

public class FrequencyBinAlgorithm extends StegAlgorithm {

	// The default size of the FFT
	// The large this value the more reliable but the less data that can be fit
	private final int FFT_SIZE = 512;
	
	/* The frequency bins to increase the amplitude in
	 * need to be very high or very low,
	 * but still within the range of mp3 conversion so data isn't lost
	 */
	// 1BIT default frequency bin : about 15500Hz 
	private final int FREQ_INDEX_1 = 180;
	// 0BIT default frequency bin : about 16020Hz
	private final int FREQ_INDEX_0 = 186;
	
	// default level to scale amplitude by
	// The larger the value the more noticable the noise but the more reliable the data
	private final int SCALE_LEVEL = 10;

	
	// FFT samples size
	private int fftSize;
	// arrays of frequency bins to use
	// can encode 1 bit in multiple bins for redundancy
	private int[] freqBins1Bit;
	private int[] freqBins0Bit;
	
	// Level to scale by
	private int scaleLevel;
	
	public FrequencyBinAlgorithm() {
		this.fftSize = FFT_SIZE;
		this.freqBins1Bit = new int[] { FREQ_INDEX_1 };
		this.freqBins0Bit = new int[] { FREQ_INDEX_0 };
		this.scaleLevel = SCALE_LEVEL;
	}
	
	@Override
	public boolean encode() {

		WaveFileInput inputAudioFile = this.getInputAudioFile();
		File messageFile = this.getMessageFile();
		WaveFileOutput outputAudioFile = this.getOutputAudioFile();

		try {
			InputStream messageFileStream = new FileInputStream(messageFile);

			byte[] messageFileBytes = new byte[(int) messageFile.length()];
			messageFileStream.read(messageFileBytes);

			// loop for every Byte in the hidden message
			for (int i = 0; i < messageFile.length(); i++) {

				byte messageByte = messageFileBytes[i];
				BitSet messageByteBits = DataUtil.byteToBitSet(messageByte);

				// new sample size
				int[] newSamples = new int[this.getFftSize() * 2];

				// loop through the bitset getting each Bit
				// Commented out for testing
				for (int bitPos = 0; bitPos < Byte.SIZE; bitPos++) {

					// read in samples set number of samples
					int[] inputSample = inputAudioFile.readSamplesUnscaled(this.getFftSize());
					int[] inputSamplesLeft = WaveFileUtil.extractLeftSamples(
							inputSample, inputAudioFile.getAudioFormat());
					int[] inputSamplesRight = WaveFileUtil.extractRightSamples(
							inputSample, inputAudioFile.getAudioFormat());

					int[] newLeftSamples;
					// if bit is a 1 then do 1 bit method on left samples
					// TODO: work with right samples too
					if (messageByteBits.get(bitPos)) {
						newLeftSamples = oneBitEncode(inputSamplesLeft);
					}
					// else do 0 bit method on left samples
					else {
						newLeftSamples = zeroBitEncode(inputSamplesLeft);
					}

					newSamples = WaveFileUtil.combineSamples(newLeftSamples,
							inputSamplesRight);

					// write the samples for that bit
					outputAudioFile.writeUnscaledSamples(newSamples);
				}

			}

			// write rest of file
			outputAudioFile.writeBytes(inputAudioFile.readRestOfByteData());
			// close the output wave file
			outputAudioFile.closeWaveFile();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private int[] bitEncode(int[] samplesArray, int[] frequencyBins)
			throws Exception {

		Complex[] samplesFft = Fft.fft(WaveFileUtil.scaleSamples(samplesArray));

		// use the 10x the AVG value (less noticable, less reliable)
		double avg = WaveFileUtil.getAverageMag(samplesFft);
		Complex newValue;
		if (avg != 0.0)
			newValue = new Complex(avg * this.getScaleLevel(), 0);
		else
			newValue = new Complex(.1, 0);

		for (int binIndex : frequencyBins) {
			samplesFft[binIndex] = newValue;
		}

		Complex[] samplesFftInv = Fft.fftInverse(samplesFft);
		double[] fftInvReals = WaveFileUtil.complexArrayGetReals(samplesFftInv);
		return WaveFileUtil.unscaleSamples(fftInvReals);

	}

	private int[] oneBitEncode(int[] samplesArray) throws Exception {
		return bitEncode(samplesArray, this.getFreqBins1Bit());
	}

	private int[] zeroBitEncode(int[] samplesArray) throws Exception {
		return bitEncode(samplesArray, this.getFreqBins0Bit());
	}

	@Override
	public boolean decode() {

		WaveFileInput inputAudioFile = this.getInputAudioFile();
		File outputMessageFile = this.getMessageFile();

		try {
			OutputStream outputFileStream = new FileOutputStream(
					outputMessageFile);

			// loop until condition is met (3 consecutive nulls or end of file)
			boolean stillValid = true;
			int nullCount = 0;
			while (stillValid) {
				BitSet messageBitSet = new BitSet();

				for (int i = 0; i < Byte.SIZE; i++) {
					// read in the constant FFT size of samples
					int[] inputSample = inputAudioFile.readSamplesUnscaled(this.getFftSize());
					int[] inputSamplesLeft = WaveFileUtil.extractLeftSamples(
							inputSample, inputAudioFile.getAudioFormat());

					// Currently only encoding in left channel
					// int[] inputSamplesRight =
					// WaveFileUtil.extractRightSamples(inputSample,
					// inputAudioFile.getAudioFormat());

					Boolean extractedBit = bitDecode(inputSamplesLeft);
					// check if the extracted bit is null (4 times kill loop)
					if (extractedBit == null) {
						nullCount++;
						if (nullCount >= 8) {
							stillValid = false;
							break;
						}
					} else {
						nullCount = 0;
						// add the extracted bit to the byte bitset value
						messageBitSet.set(i, extractedBit);
					}
				}
				// if we are invalid then don't write last bit
				if (stillValid) {
					// if looped 8 times then write that byte to file
					int messageByte = DataUtil.bitSetToInt(messageBitSet);

					outputFileStream.write(messageByte);
				}

			}

			outputFileStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private Boolean bitDecode(int[] samples) throws Exception {
		Complex[] samplesFft = Fft.fft(WaveFileUtil.scaleSamples(samples));

		double sampleAvg = WaveFileUtil.getAverageMag(samplesFft);
		double sampleAvg2X = sampleAvg * 1.5;

		int oneBitCount = 0;
		double oneBitMax = 0;
		int zeroBitCount = 0;
		double zeroBitMax = 0;
		// loop through frequency bins
		for (int i = 0; i < this.getFreqBins1Bit().length; i++) {
			// get the power magnitude at the 1 bit freq
			double oneBit = samplesFft[this.getFreqBins1Bit()[i]]
					.magnitude();

			// get the power magnitude at the 0 bit freq
			double zeroBit = samplesFft[this.getFreqBins0Bit()[i]]
					.magnitude();

			if (oneBit > sampleAvg2X) {
				if (oneBit > oneBitMax)
					oneBitMax = oneBit;
				oneBitCount++;
			}
			if (zeroBit > sampleAvg2X) {
				if (zeroBit > zeroBitMax)
					zeroBitMax = zeroBit;
				zeroBitCount++;
			}
		}

		if (oneBitCount >= this.getFreqBins1Bit().length
				|| zeroBitCount >= this.getFreqBins0Bit().length) {
			if (oneBitCount == zeroBitCount) {
				return (oneBitMax > zeroBitMax);
			}
			return (oneBitCount > zeroBitCount);
		}

		return null;
	}

	@Override
	public boolean willItFit(WaveFileInput inputAudioFile, File messageFile) {
		long sampleNumber = inputAudioFile.getAudioStream().getFrameLength();
		long messageBitNumber = messageFile.length() * Byte.SIZE;
		return ((sampleNumber / this.getFftSize()) - messageBitNumber) > 0;
	}

	public int getFftSize() {
		return fftSize;
	}

	public void setFftSize(int fftSize) {
		this.fftSize = fftSize;
	}

	public int[] getFreqBins1Bit() {
		return freqBins1Bit;
	}

	public void setFreqBins1Bit(int[] freqBins1Bit) {
		this.freqBins1Bit = freqBins1Bit;
	}

	public int[] getFreqBins0Bit() {
		return freqBins0Bit;
	}

	public void setFreqBins0Bit(int[] freqBins0Bit) {
		this.freqBins0Bit = freqBins0Bit;
	}

	public int getScaleLevel() {
		return scaleLevel;
	}

	public void setScaleLevel(int scaleLevel) {
		this.scaleLevel = scaleLevel;
	}

	

}
