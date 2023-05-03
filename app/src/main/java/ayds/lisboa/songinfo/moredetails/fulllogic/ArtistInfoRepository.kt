package ayds.lisboa.songinfo.moredetails.fulllogic

import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo

interface ArtistInfoRepository{
    fun getArtistInfo(artistName: String): ArtistInfo
}

internal class ArtistInfoRepositoryImpl(
    private val lastFMLocalStorage: LastFMLocalStorage,
    private val lastFMService: LastFMService
) : ArtistInfoRepository {


    override fun getArtistInfo(artistName: String): ArtistInfo {
        var artistInfo = lastFMLocalStorage.getArtistInfo(artistName)

        when {
            artistInfo != null -> markArtistInfoAsSavedDB(artistInfo)
            else -> {
                try {
                    artistInfo = lastFMService.getArtistInfo(artistName)
                    saveArtistInfoDB(artistName, artistInfo)
                } catch (ioException: Exception) {
                    ioException.printStackTrace()
                }
            }
        }

        return artistInfo ?: ArtistInfo.EmptyArtistInfo
    }

    private fun markArtistInfoAsSavedDB(artistInfo: LastFmArtistInfo) {
        artistInfo.isLocallyStored = true
    }

    private fun saveArtistInfoDB(artistName: String, artistInfo: LastFmArtistInfo) {
        lastFMLocalStorage.saveArtist(artistName, artistInfo)
    }


}