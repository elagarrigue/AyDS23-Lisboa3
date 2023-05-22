package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.EmptyCard
import ayds.lisboa.songinfo.moredetails.data.local.LastFmLocalStorage
import ayds.lisboa3.submodule.lastFm.external.LastFmService
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.lisboa3.submodule.lastFm.external.LastFmArtistInfo

const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

internal class ArtistInfoRepositoryImpl(
    private val lastFmLocalStorage: LastFmLocalStorage,
    private val lastFmService: LastFmService
) : ArtistInfoRepository {

    override fun getArtistInfo(artistName: String): Card {
        var artistCard = lastFmLocalStorage.getArtistCard(artistName)

        when {
            artistCard != null -> markArtistInfoAsLocal(artistCard)
            else -> {
                try {
                    val lastFmArtistInfo = lastFmService.getArtistInfo(artistName)
                    artistCard = adaptLastFmArtistInfoToCard(lastFmArtistInfo)
                    artistCard?.let{saveArtistInfoDB(artistName, it)}
                } catch (ioException: Exception) {
                    artistCard = null
                }
            }
        }

        return artistCard ?: EmptyCard
    }

    private fun markArtistInfoAsLocal(artistInfo: ArtistCard) {
        artistInfo.isLocallyStored = true
    }

    private fun saveArtistInfoDB(artistName: String, artistInfo: ArtistCard) {
        lastFmLocalStorage.saveArtistCard(artistName, artistInfo)
    }

    private fun adaptLastFmArtistInfoToCard(lastFmArtistInfo: LastFmArtistInfo?) =
        lastFmArtistInfo?.let { ArtistCard(it.bioContent, it.url, 1, LAST_FM_DEFAULT_IMAGE) }
}