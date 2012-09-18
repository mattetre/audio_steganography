package edu.neu.ccs.kemf.algorithms;

public class FrequencyBinAlgorithm_256_HI extends FrequencyBinAlgorithm {

	public FrequencyBinAlgorithm_256_HI() {
		this.setFftSize(256);
		this.setFreqBins1Bit(new int[] { 90 });
		this.setFreqBins0Bit(new int[] { 95 });
		
	}
	
}
