package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${getSongDate(song.releaseDate, song.releaseDatePrecision)}"
            else -> "Song not found"
        }
    }

    private fun getSongDate(releaseDate: String, releaseDatePrecision: String): String {
        val songDateHelper:SongDateHelper = SongDateHelperImpl(releaseDate, releaseDatePrecision)
        return songDateHelper.getFormat()
    }
}