package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.local.LastFmLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa3.submodule.lastFm.external.LastFmService
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.lisboa3.submodule.lastFm.external.LastFmArtistInfo

const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

internal class ArtistInfoRepositoryImpl(
    private val lastFmLocalStorage: LastFmLocalStorage,
    private val lastFmService: LastFmService
) : ArtistInfoRepository {

    override fun getArtistInfo(artistName: String): List<Card> {
        val artistCards = lastFmLocalStorage.getArtistCards(artistName).toMutableList()

        when {
            artistCards.isNotEmpty() -> markArtistCardsAsLocal(artistCards)
            else -> {
                try {
                    val lastFmArtistInfo = lastFmService.getArtistInfo(artistName)
                    val lastFmCard = adaptLastFmArtistInfoToCard(lastFmArtistInfo)
                    lastFmCard?.let { artistCards.add(it) }
                    saveCards(artistName, artistCards)
                } catch (ioException: Exception) {
                    artistCards.clear()
                }
            }
        }

        return artistCards
    }

    private fun markArtistCardsAsLocal(artistCards: List<Card>) {
        artistCards.forEach { it.isLocallyStored = true}
    }

    private fun adaptLastFmArtistInfoToCard(lastFmArtistInfo: LastFmArtistInfo?) =
        lastFmArtistInfo?.let { Card(it.bioContent, it.url, 1, LAST_FM_DEFAULT_IMAGE) }

    private fun saveCards(artistName: String, artistCards: List<Card>) {
        artistCards.forEach { lastFmLocalStorage.saveArtistCard(artistName, it) }
    }

}