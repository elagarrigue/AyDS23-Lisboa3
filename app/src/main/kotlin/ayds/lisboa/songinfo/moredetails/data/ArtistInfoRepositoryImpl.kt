package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo.LastFmArtistInfo
import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo.EmptyArtistInfo
import ayds.lisboa.songinfo.moredetails.data.local.LastFmLocalStorage
import ayds.lisboa.songinfo.moredetails.data.external.LastFmService
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository

internal class ArtistInfoRepositoryImpl(
    private val lastFMLocalStorage: LastFmLocalStorage,
    private val lastFMService: LastFmService
) : ArtistInfoRepository {

    override fun getArtistInfo(artistName: String): ArtistInfo {
        var artistInfo = lastFMLocalStorage.getArtistInfo(artistName)

        when {
            artistInfo != null -> markArtistInfoAsLocal(artistInfo)
            else -> {
                try {
                    artistInfo = lastFMService.getArtistInfo(artistName)
                    artistInfo?.let {
                        saveArtistInfoDB(artistName, artistInfo)
                    }
                } catch (ioException: Exception) {
                    ioException.printStackTrace()
                }
            }
        }

        return artistInfo ?: EmptyArtistInfo
    }

    private fun markArtistInfoAsLocal(artistInfo: LastFmArtistInfo) {
        artistInfo.isLocallyStored = true
    }

    private fun saveArtistInfoDB(artistName: String, artistInfo: LastFmArtistInfo) {
        lastFMLocalStorage.saveArtist(artistName, artistInfo)
    }

}