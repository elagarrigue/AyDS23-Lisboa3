package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.external.broker.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.local.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.repository.CardsRepository

internal class CardsRepositoryImpl(
    private val cardsLocalStorage: CardsLocalStorage,
    private val cardsBroker: CardsBroker
) : CardsRepository {

    override fun getArtistCards(artistName: String): List<ArtistCard> {
        var artistCards = cardsLocalStorage.getArtistCards(artistName)

        when {
            artistCards.isNotEmpty() -> markArtistCardsAsLocal(artistCards)
            else -> {
                artistCards = cardsBroker.getArtistCards(artistName)
                saveArtistCards(artistName, artistCards)
            }
        }

        return artistCards
    }

    private fun markArtistCardsAsLocal(artistCards: List<ArtistCard>) {
        artistCards.forEach { it.isLocallyStored = true }
    }

    private fun saveArtistCards(artistName: String, artistCards: List<ArtistCard>) {
        artistCards.forEach { cardsLocalStorage.saveArtistCard(artistName, it) }
    }

}