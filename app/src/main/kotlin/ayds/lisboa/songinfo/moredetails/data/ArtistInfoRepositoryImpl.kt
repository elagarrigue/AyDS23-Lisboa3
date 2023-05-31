package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.external.broker.Broker
import ayds.lisboa.songinfo.moredetails.data.local.CardLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository

internal class ArtistInfoRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : ArtistInfoRepository {

    override fun getArtistInfo(artistName: String): List<ArtistCard> {
        var artistCards = cardLocalStorage.getArtistCards(artistName)

        when {
            artistCards.isNotEmpty() -> markArtistCardsAsLocal(artistCards)
            else -> {
                artistCards = broker.getCards(artistName)
                saveArtistCards(artistName, artistCards)
            }
        }

        return artistCards
    }

    private fun markArtistCardsAsLocal(artistCards: List<ArtistCard>) {
        artistCards.forEach { it.isLocallyStored = true }
    }

    private fun saveArtistCards(artistName: String, artistCards: List<ArtistCard>) {
        artistCards.forEach { cardLocalStorage.saveArtistCard(artistName, it) }
    }

}