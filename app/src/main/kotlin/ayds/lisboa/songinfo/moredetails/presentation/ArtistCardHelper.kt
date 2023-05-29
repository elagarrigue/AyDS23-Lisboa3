package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard

interface ArtistCardHelper {
    fun getArtistCards(artistName: String, artistCards: List<Card>): List<ArtistCard>
}

private const val DB_SAVED_SYMBOL = "[*]"
private const val NO_RESULTS = "No Results"

internal class ArtistCardHelperImpl(private val htmlHelper: HtmlHelper) : ArtistCardHelper {

    override fun getArtistCards(artistName: String, artistCards: List<Card>): List<ArtistCard> {
        return artistCards.filterIsInstance<ArtistCard>().map { card ->
            card.copy(description = formatDescription(card.description, artistName))
        }
    }

    private fun formatDescription(description: String, artistName: String): String {
        val dbSaved = if (description.isNotEmpty()) DB_SAVED_SYMBOL else ""
        val descriptionFormatted = if (description.isEmpty()) NO_RESULTS else htmlHelper.getHtmlText(description, artistName)

        return dbSaved + descriptionFormatted
    }
}
