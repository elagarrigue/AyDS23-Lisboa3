package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface ArtistCardHelper {
    fun getLastFmCard(artistName: String, artistCards: List<Card>): Card
    fun getNewYorkTimesCard(artistName: String, artistCards: List<Card>): Card
    fun getWikipediaCard(artistName: String, artistCards: List<Card>): Card
}

private const val DB_SAVED_SYMBOL = "[*]"
private const val NO_RESULTS = "No Results"

internal class ArtistCardHelperImpl(private val htmlHelper: HtmlHelper): ArtistCardHelper {
    override fun getLastFmCard(artistName: String, artistCards: List<Card>): Card {
        val lastFmCard = artistCards.find { it.source == Source.LastFm }

        lastFmCard?.let {it.description = it.formatDescription(artistName)}

        return lastFmCard ?: Card(source = Source.LastFm)
    }

    override fun getNewYorkTimesCard(artistName: String, artistCards: List<Card>): Card {
        val newYorkTimesCard = artistCards.find { it.source == Source.NewYorkTimes }

        newYorkTimesCard?.let {it.description = it.formatDescription(artistName)}

        return newYorkTimesCard ?: Card(source = Source.NewYorkTimes)
    }

    override fun getWikipediaCard(artistName: String, artistCards: List<Card>): Card {
        val wikipediaCard = artistCards.find { it.source == Source.Wikipedia }

        wikipediaCard?.let {it.description = it.formatDescription(artistName)}

        return wikipediaCard ?: Card(source = Source.Wikipedia)
    }

    private fun Card.formatDescription(artistName: String): String {
        val dbSaved = if (isLocallyStored) DB_SAVED_SYMBOL else ""
        val descriptionFormatted = if (description.isEmpty()) NO_RESULTS else htmlHelper.getHtmlText(description, artistName)

        return dbSaved + descriptionFormatted
    }

}