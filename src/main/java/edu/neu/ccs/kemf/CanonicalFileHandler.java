package edu.neu.ccs.kemf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class CanonicalFileHandler {
	
	private static final String TEMP_DIR = "temp";
	private static final String TEMP_CANONICAL = "canonical.wav";
	private static final String TEMP_ENCODING = "encoding.wav";
	
	File inputFile;
	File canonicalFile;
	File encodingFile;
	File outputFile;
	Mp3IdInfo mp3Tags;
	boolean needToConvert;
	
	private CanonicalFileHandler(File inputFile, File canonicalFile, File encodingFile, File outputFile, Mp3IdInfo mp3Tags, boolean needToConvert) {
		this.inputFile = inputFile;
		this.canonicalFile = canonicalFile;
		this.encodingFile = encodingFile;
		this.outputFile = outputFile;
		this.mp3Tags = mp3Tags;
		this.needToConvert = needToConvert;
	}

	/**
	 * Create a new Canonical File Handler to handle <b>only input</b>
	 * 
	 * @param inputFilePath The file path of the input file
	 * @return A new canonical file with valid inputFile and canonicalFile 
	 * @throws Exception 
	 * @throws FileNotFoundException If the inputFilePath is not found
	 */
	public static CanonicalFileHandler createDecodingCanonicalFile(File inputFile) throws Exception, IOException {
		CanonicalFileHandler canonicalFile = createEncodingCanonicalFile(inputFile, null);
		canonicalFile.setEncodingFile(null);
		canonicalFile.setOutputFile(null);
		return canonicalFile;
	}
	
	/**
	 * Create a new Canonical File Handler to handle input and output
	 * 
	 * @param inputFilePath The input file path
	 * @param outputFilePath The output file path
	 * @return A new CanonicalFileHandler for input and output
	 * @throws Exception 
	 * @throws FileNotFoundException
	 */
	public static CanonicalFileHandler createEncodingCanonicalFile(File inputFile, File outputFile) throws Exception, IOException {
		// defaults
		File canonicalFile = null;
		File encodingFile = null;
		Mp3IdInfo mp3Tags = null;
		boolean needToConvert = false;
		
		// create output folder if 
		if (outputFile != null) {
			File parent = outputFile.getParentFile();
			if (parent != null)
				outputFile.getParentFile().mkdirs();
		}

		try {
			// try to get the input file format
			AudioFileFormat inputFormat = AudioSystem.getAudioFileFormat(inputFile);
			// if the file is in WAVE format
			if (inputFormat.getType().equals(AudioFileFormat.Type.WAVE)) {
				// since we have a wave input the canonical file is same as the input
				canonicalFile = inputFile;
				// encoding file is same as output
				encodingFile = outputFile;
			}				
		} 
		// TODO: Validate MP3 input (LAME doesn't recognize unless ext is .mp3)
		// catch unsupported file exception (such as MP3)
		catch (UnsupportedAudioFileException e) {
			
			// extract the MP3 tags here
			mp3Tags = Mp3IdExtractor.extractMp3Info(inputFile);
			
			// try to use lame to convert this file
			File tempDir = new File(TEMP_DIR);
			tempDir.mkdirs();
			
			canonicalFile = new File(tempDir, TEMP_CANONICAL);
			// try to convert file
			boolean success = Converter.convertMp3ToWave(inputFile.getPath(), canonicalFile.getPath());
			if (!success)
				throw new Exception("Audio conversion failed");
			
			// set the encoding file to a temp file
			encodingFile = new File(TEMP_DIR, TEMP_ENCODING);
			
			needToConvert = true;
		}
		
		return new CanonicalFileHandler(inputFile, canonicalFile, encodingFile, outputFile, mp3Tags, needToConvert);
	}
	
	/**
	 * Make sure the output file is converted if necessary
	 * 
	 * @return True if output file conversion completes false otherwise
	 */
	public boolean completeOutputFile() {
		if (this.needToConvert) {
			try {
				// convert wave file to mp3 
				return Converter.convertWaveToMp3(this.getEncodingFile().getPath(), this.getOutputFile().getPath(), this.getMp3Tags());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;
	}
	
	/**
	 * Delete temporary directory
	 * @return True if delete succeeds false otherwise
	 */
	public boolean cleanUp() {
 		// remove temp dir
		File tempDir = new File(TEMP_DIR);
		return FileUtil.deleteDirectory(tempDir);
	}
	
	
	public File getInputFile() {
		return this.inputFile;
	}
	
	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}
	
	public File getCanonicalFile() {
		return this.canonicalFile;
	}
	
	public void setCanonicalFile(File canonicalFile) {
		this.canonicalFile = canonicalFile;
	}
	
	public File getEncodingFile() {
		return this.encodingFile;
	}
	
	public void setEncodingFile(File encodingFile) {
		this.encodingFile = encodingFile;
	}
	
	public File getOutputFile() {
		return this.outputFile;
	}
	
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	protected Mp3IdInfo getMp3Tags() {
		return mp3Tags;
	}

	protected void setMp3Tags(Mp3IdInfo mp3Tags) {
		this.mp3Tags = mp3Tags;
	}

}
