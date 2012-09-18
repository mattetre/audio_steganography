package edu.neu.ccs.kemf;

/**
 * Class to hold mp3 tag data
 */
public class Mp3IdInfo {

	private String artist;
	private String album;
	private String title;
	private String bitrate;
	
	public Mp3IdInfo(String artist, String album, String title, String bitrate) {
		this.artist = artist;
		this.album = album;
		this.title = title;
		this.bitrate = bitrate;
	}
	
	public Mp3IdInfo() {
		this.artist = null;
		this.album = null;
		this.title = null;
		this.bitrate = null;
	}

	protected String getArtist() {
		return artist;
	}

	protected void setArtist(String artist) {
		this.artist = artist;
	}

	protected String getAlbum() {
		return album;
	}

	protected void setAlbum(String album) {
		this.album = album;
	}

	protected String getTitle() {
		return title;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected String getBitrate() {
		return bitrate;
	}

	protected void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}
	
	
}
