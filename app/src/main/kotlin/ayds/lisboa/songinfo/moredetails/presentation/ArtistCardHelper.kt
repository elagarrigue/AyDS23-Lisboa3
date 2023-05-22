package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ArtistCardHelper {
    fun getArtistCardDescription(artistName: String, card: Card): String
    fun getArtistCardInfoUrl(card: Card): String
    fun getArtistCardSourceLogo(card: Card): String
}

private const val DB_SAVED_SYMBOL = "[*]"
private const val NO_ARTIST_CARD_FOUND = "Artist card not found."
private const val NO_RESULTS_DESCRIPTION = "No Results"
private const val NO_RESULTS_INFO_URL = "No Results (infoUrl)"
private const val NO_RESULTS_SOURCE_LOGO = "No Results (sourceLogo)"


internal class ArtistCardHelperImpl(private val htmlHelper: HtmlHelper): ArtistCardHelper {

    override fun getArtistCardDescription(artistName: String, card: Card): String {
        return when (card) {
            is Card.ArtistCard -> card.formatDescription(artistName)
            else -> NO_ARTIST_CARD_FOUND
        }
    }

    private fun Card.ArtistCard.formatDescription(artistName: String): String {
        val dbSaved = if (isLocallyStored) DB_SAVED_SYMBOL else ""
        val descriptionFormatted = if (description.isEmpty()) NO_RESULTS_DESCRIPTION else htmlHelper.getHtmlText(description, artistName)

        return dbSaved + descriptionFormatted
    }

    override fun getArtistCardInfoUrl(card: Card): String {
        return when (card) {
            is Card.ArtistCard -> card.infoUrl
            else -> NO_RESULTS_INFO_URL
        }
    }

    override fun getArtistCardSourceLogo(card: Card): String {
        return when (card) {
            is Card.ArtistCard -> card.sourceLogo
            else -> NO_RESULTS_SOURCE_LOGO
        }
    }

}