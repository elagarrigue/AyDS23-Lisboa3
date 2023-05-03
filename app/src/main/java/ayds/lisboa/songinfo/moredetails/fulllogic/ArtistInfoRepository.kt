package ayds.lisboa.songinfo.moredetails.fulllogic

import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo

interface ArtistInfoRepository{
    fun getArtistInfo(artistName: String): ArtistInfo?
}

internal class ArtistInfoRepositoryImpl(
    private val lastFMLocalStorage: LastFmLocalStorage,
    private val lastFMService: LastFmService
) : ArtistInfoRepository {


    override fun getArtistInfo(artistName: String): ArtistInfo {
        var artistInfo = lastFMLocalStorage.getArtistInfo(artistName)

        when {
            artistInfo != null -> markArtistInfoAsSavedDB(artistInfo)
            else -> {
                try {
                    artistInfo = lastFMService.getArtistInfo(artistName)
                    saveArtistInfoDB(artistName, artistInfo)
                    (artistInfo as? ArtistInfo.LastFmArtistInfo)?.let {
                        when {
                            it.isSavedSong() -> lastFMLocalStorage.updateSongTerm(term, it.id)
                        }
                    }

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