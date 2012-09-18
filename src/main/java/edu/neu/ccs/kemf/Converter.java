package edu.neu.ccs.kemf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Converter {
	
	// lame encoder and decoder options
	private static final String LAME_ENC_PATH = "/course/cs4500wc/bin/lame";
	//private static final String LAME_ENC_PATH = "/usr/bin/lame";
	private static final String BITRATE_OPTION = "-b";
	private static final String OUTPUT_OPTION = "-o";
	private static final String DECODE_OPTION = "--decode";
	
	// mp3 tag info
	private static final String MP3_TITLE_OPTION = "--tt";
	private static final String MP3_ARTIST_OPTION = "--ta";
	private static final String MP3_ALBUM_OPTION = "--tl";
	
	// default bitrate to use
	private static final String DEFAULT_BITRATE = "160";
	
	/**
	 * Convert the given MP3 file to canonical format
	 * @param mp3FilePath The mp3 file path to convert
	 * @param fileOutputPath The output file path
	 * @return True if conversion success false otherwise
	 */
	public static boolean convertMp3ToWave(String mp3FilePath, String fileOutputPath) throws IOException {
		// run the lame decoder
		return executeLameDecode(mp3FilePath, fileOutputPath);
	}
	
	/**
	 * Convert the wave file to an mp3 file
	 * @param wavFilePath The wave file path to convert
	 * @param fileOutputPath The output path
	 * @param mp3Tags The mp3 id info to write or null to exclude
	 * @return True if conversion success false otherwise
	 */
	public static boolean convertWaveToMp3(String wavFilePath, String fileOutputPath, Mp3IdInfo mp3Tags) throws IOException {
		// run the lame decoder
		return executeLameEncode(wavFilePath, fileOutputPath, mp3Tags);
	}
	
	/**
	 * Execute the lame encoder
	 * @param inputFilePath
	 * @param outputFilePath
	 * @param mp3Tags
	 * @return
	 * @throws IOException
	 */
	private static boolean executeLameEncode(String inputFilePath, String outputFilePath, Mp3IdInfo mp3Tags) throws IOException {
		List<String> cmds = new ArrayList<String>();
		cmds.add(LAME_ENC_PATH);
		// set the input file
		cmds.add(inputFilePath);
		// set the output file
		cmds.add(OUTPUT_OPTION);
		cmds.add(outputFilePath);
		
		// if mp3tags exist then use them
		if (mp3Tags != null) {
			// set the bitrate option
			cmds.add(BITRATE_OPTION);
			cmds.add(mp3Tags.getBitrate());
			
			// get mp3 artist tag
			String artist = mp3Tags.getArtist();
			if (artist != "" && artist != null) {
				cmds.add(MP3_ARTIST_OPTION);
				cmds.add(artist);
			}
			// get mp3 title tag
			String title = mp3Tags.getTitle();
			if (title != "" && title != null) {
				cmds.add(MP3_TITLE_OPTION);
				cmds.add(title);
			}
			// get mp3 album tag
			String album = mp3Tags.getAlbum();
			if (title != "" && title != null) {
				cmds.add(MP3_ALBUM_OPTION);
				cmds.add(album);
			}
		}
		// otherwise use default bitrate
		else {
			cmds.add(BITRATE_OPTION);
			cmds.add(DEFAULT_BITRATE);
		}


		return SystemUtil.executeCmd(cmds);
		
	}
	
	/**
	 * Execute lame decoder
	 * @param inputFilePath
	 * @param outputFilePath
	 * @return
	 * @throws IOException
	 */
	private static boolean executeLameDecode(String inputFilePath, String outputFilePath) throws IOException {
		List<String> cmds = new ArrayList<String>();
		cmds.add(LAME_ENC_PATH);
		// set the input file
		cmds.add(inputFilePath);
		// set the decode option
		cmds.add(DECODE_OPTION);
		// set the output file
		cmds.add(OUTPUT_OPTION);
		cmds.add(outputFilePath);
		
		return SystemUtil.executeCmd(cmds);
		
	}
	

}
