package edu.neu.ccs.kemf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mp3IdExtractor {

	// python command
	private static final String PYTHON = "python";
	// the name of script to run for mp3tags
	private static final String MP3_EXTRACT_SCRIPT = "mp3tags.py";
	
	// elements returned by the script
	private static final String MP3_BITRATE = "bitrate";
	private static final String MP3_TITLE = "title";
	private static final String MP3_ARTIST = "artist";
	private static final String MP3_ALBUM = "album";
	
	/**
	 * Extract the MP3 tags from a given mp3 file
	 * @param mp3File The MP3 file to get tags from
	 * @return An Mp3IdInfo object if successfull or null otherwise
	 */
	public static Mp3IdInfo extractMp3Info(File mp3File) {
		// try to find the mp3 extract script
		String mp3ExtractScript = SystemUtil.getTargetFilePath(MP3_EXTRACT_SCRIPT);
		// if the script can't be found just return null
		if (mp3ExtractScript == null)
			return null;
		
		
		List<String> cmds = new ArrayList<String>(0);
		cmds.add(PYTHON);
		cmds.add(mp3ExtractScript);
		cmds.add(mp3File.getPath());
		
		try {
			String mp3TagResult = SystemUtil.executeCmdGetReturn(cmds);
			return parseResult(mp3TagResult);
		} catch (IOException e) {
			// error durring extracting tags, ignore and return null
			return null;
		}
	}	
	
	/**
	 * Parse the results of the mp3tag script
	 * @param mp3TagResult The result of the script
	 * @return An Mp3IdInfo object or null if error parsing
	 */
	private static Mp3IdInfo parseResult(String mp3TagResult) {
		Mp3IdInfo mp3Info = new Mp3IdInfo();
		
		// split results by new line
		String[] lineSplit = mp3TagResult.split("\n");
		// loop through lines
		for (String line : lineSplit) {
			// split line by :
			String[] tagSplit = line.split(":");
			// expecting 2 elements (key and value)
			if (tagSplit.length == 2) {
				String key = tagSplit[0];
				String value = tagSplit[1];
				
				if (key.equalsIgnoreCase(MP3_BITRATE))
					mp3Info.setBitrate(value);
				else if (key.equalsIgnoreCase(MP3_TITLE))
					mp3Info.setTitle(value);
				else if (key.equalsIgnoreCase(MP3_ALBUM))
					mp3Info.setAlbum(value);
				else if (key.equalsIgnoreCase(MP3_ARTIST))
					mp3Info.setArtist(value);
			}
		}
		
		return mp3Info;
	}
	
}
