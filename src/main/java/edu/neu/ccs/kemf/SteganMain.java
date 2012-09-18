package edu.neu.ccs.kemf;

import java.io.File;

import edu.neu.ccs.kemf.algorithms.AvailableAlgorithms;

/**
 * Main class for stegan entry
 */
public class SteganMain {
	
	// The encode option
	private static final String ENCODE = "--encode";
	// The decode option
	private static final String DECODE = "--decode";
	
	// The algorithm to use for the encoding and decoding
	private static final AvailableAlgorithms algorithm = AvailableAlgorithms.FREQUENCY_BIN_SET_512_HI;
	
	/**
	 * Main entry point
	 */
	public static void main(String[] args) {
				
		// check argument list
		int argLength = args.length;
		if (argLength < 3) {
			invalidArguments();
		}
		
		// get the operation
		String operation = args[0];
		File carrierFile = new File(args[1]);
		
		boolean success = false;
		// if using encode option
		if (operation.equalsIgnoreCase(ENCODE)) {
			if (argLength != 4)
				invalidArguments();
			
			File messageToEncode = new File(args[2]);
			File outputFile = new File(args[3]);
			
			// check that the files exist
			if (!carrierFile.exists())
				printErrorAndExit("File not found: " + carrierFile);
			if (!messageToEncode.exists())
				printErrorAndExit("File not found: " + messageToEncode);
			
			try {
				Steganographer stegan = Steganographer.createEncodingSteganographer(carrierFile, outputFile, messageToEncode, algorithm);
				success = stegan.encode();
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		// if using the decode option
		else if (operation.equalsIgnoreCase(DECODE)) {
			if (argLength != 3)
				invalidArguments();
			
			File outputFile = new File(args[2]);
			
			// check that the files exist
			if (!carrierFile.exists())
				printErrorAndExit("File not found: " + carrierFile);
			
			try {
				Steganographer stegan = Steganographer.createDecodingSteganographer(carrierFile, outputFile, algorithm );
				success = stegan.decode();	
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// otherwise print out error
		else {
			invalidArguments();
		}
		
		if (!success)
			System.err.println("There was an error during: " + operation);
	}

	/**
	 * Print invalid argument error, program usage and exit
	 */
	private static void invalidArguments() {
		printErrorUsageAndExit("Invalid Arguments");
	}
	
	/**
	 * Print error message, the usage of the program and exit
	 * @param error The error message to print
	 */
	private static void printErrorUsageAndExit(String error) {
		System.err.println(error);
		printUsage();		
		System.exit(1);
	}

	/**
	 * Print error message and exit
	 * @param error The error message to print
	 */
	private static void printErrorAndExit(String error) {
		System.err.println(error);
		System.exit(1);
	}

	/**
	 * Print the usage of the program
	 */
	private static void printUsage() {
		System.out.println(
				"Usage: stegan --encode CARRIER_FILE MESSAGE_FILE OUTPUT_FILE\n" +
				"  or:  stegan --decode CARRIER_FILE OUTPUT_FILE");
	}
	
	
	
	
	
	
	

}
