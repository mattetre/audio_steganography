from mutagen.mp3 import MP3
import sys

# read the mp3 file name from the command line
mp3file =  sys.argv[1]
audio = MP3(mp3file)

titleKey = "TIT2"
artistKey = "TPE1"
albumKey = "TALB"

bitrate = audio.info.bitrate / 1000
print "bitrate:" + str(bitrate)

if titleKey in audio:
	title = audio[titleKey]
	print "title:" + str(title)
if artistKey in audio:
	artist = audio[artistKey]
	print "artist:" + str(artist)
if albumKey in audio:
	album = audio[albumKey]
	print "album:" + str(album)
