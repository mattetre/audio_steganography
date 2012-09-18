package edu.neu.ccs.kemf;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {

	static private final String LOG_FILE = "stegan.log";
	
	static private FileHandler logFileHandler;
	static private SimpleFormatter logFileFormatter;
	
	static public void initialize() {
		
		try {
			// create new logger
			Logger logger = Logger.getLogger("");
	
			// debugging level
			logger.setLevel(Level.CONFIG);
			
			logFileHandler = new FileHandler(LOG_FILE);
			logFileFormatter = new SimpleFormatter();
			logFileHandler.setFormatter(logFileFormatter);
			logger.addHandler(logFileHandler);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
