package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo

interface ArtistInfoRepository{
    fun saveArtist(artist: String, artistInfo: LastFmArtistInfo)
    fun getArtistInfo(artist: String): LastFmArtistInfo?
}

internal class ArtistInfoRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
) : ArtistInfoRepository {


    override fun saveArtist(artist: String, artistInfo: LastFmArtistInfo) {
        val artistValues = getArtistValues(artist, artistInfo)
        lastFMLocalStorage.saveArtist(artistValues)
    }

    private fun getArtistValues(artist: String, artistInfo: LastFmArtistInfo): ContentValues {
        val values = ContentValues().apply {
            put(ARTIST, artist)
            put(BIO_CONTENT, artistInfo.bioContent)
            put(URL, artistInfo.url)
            put(SOURCE, 1)
        }

        return values
    }

    override fun getArtistInfo(artist: String): LastFmArtistInfo? {
        val artistValues = arrayOf(artist)

        return lastFMLocalStorage.getArtistInfo(artistValues)
    }




}