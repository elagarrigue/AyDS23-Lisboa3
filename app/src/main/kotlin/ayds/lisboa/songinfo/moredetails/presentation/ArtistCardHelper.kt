package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ArtistCardHelper {
    fun getLastFmCard(artistName: String, artistCards: List<Card>): Card
    fun getNewYorkTimesCard(artistName: String, artistCards: List<Card>): Card
    fun getWikipediaCard(artistName: String, artistCards: List<Card>): Card
}

private const val DB_SAVED_SYMBOL = "[*]"
private const val NO_RESULTS = "No Results"

internal class ArtistCardHelperImpl(private val htmlHelper: HtmlHelper): ArtistCardHelper {
    override fun getLastFmCard(artistName: String, artistCards: List<Card>): Card {
        val lastFmCard = artistCards.component1()

        lastFmCard.description = formatDescription(lastFmCard, artistName)

        return lastFmCard
    }

    override fun getNewYorkTimesCard(artistName: String, artistCards: List<Card>): Card {
        val newYorkTimesCard = artistCards.component2()

        newYorkTimesCard.description = formatDescription(newYorkTimesCard, artistName)

        return newYorkTimesCard
    }

    override fun getWikipediaCard(artistName: String, artistCards: List<Card>): Card {
        val wikipediaCard = artistCards.component3()

        wikipediaCard.description = formatDescription(wikipediaCard, artistName)

        return wikipediaCard
    }

    private fun formatDescription(card: Card, artistName: String): String {
        val dbSaved = if (card.isLocallyStored) DB_SAVED_SYMBOL else ""
        val descriptionFormatted = if (card.description.isEmpty()) NO_RESULTS else htmlHelper.getHtmlText(card.description, artistName)

        return dbSaved + descriptionFormatted
    }

}