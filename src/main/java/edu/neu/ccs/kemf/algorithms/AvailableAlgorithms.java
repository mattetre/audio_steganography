package edu.neu.ccs.kemf.algorithms;

public enum AvailableAlgorithms {
	
	FREQUENCY_BIN_SET_512_HI(new FrequencyBinAlgorithm()),
	FREQUENCY_BIN_SET_256_HI(new FrequencyBinAlgorithm_256_HI());

	
	// algorithm object
	private StegAlgorithm algorithm;	
	
	private AvailableAlgorithms(StegAlgorithm alg) {
		algorithm = alg;
	}
	
	/**
	 * Get the StegAlgorithm object from the enum
	 * @return StegAlgorithm for enum
	 */
	public StegAlgorithm getAlgorithm() {
		return this.algorithm;
	}
	
}
