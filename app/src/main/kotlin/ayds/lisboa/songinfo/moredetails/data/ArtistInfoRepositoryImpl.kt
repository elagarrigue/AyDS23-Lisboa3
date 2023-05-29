package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.external.Broker
import ayds.lisboa.songinfo.moredetails.data.local.CardLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository

internal class ArtistInfoRepositoryImpl(
    private val cardLocalStorage: CardLocalStorage,
    private val broker: Broker
) : ArtistInfoRepository {

    override fun getArtistInfo(artistName: String): List<Card> {
        var artistCards = cardLocalStorage.getArtistCards(artistName)

        when {
            artistCards.isNotEmpty() -> markArtistCardsAsLocal(artistCards)
            else -> {
                try {
                    artistCards = broker.getCards(artistName)
                    saveCards(artistName, artistCards)
                } catch (ioException: Exception) {
                    emptyList<Card>()
                }
            }
        }

        return artistCards
    }

    private fun markArtistCardsAsLocal(artistCards: List<Card>) {
        artistCards.forEach { card ->
            if (card is Card.RegularCard) {
                card.isLocallyStored = true
            }
        }
    }

    private fun saveCards(artistName: String, artistCards: List<Card>) {
        artistCards.forEach { cardLocalStorage.saveArtistCard(artistName, it) }
    }

}