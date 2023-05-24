package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ArtistCardHelper {
    fun getArtistCardDescription(artistName: String, artistCard: Card): String
    fun getArtistCardInfoUrl(artistCard: Card): String
    fun getArtistCardSource(artistCard: Card): String
    fun getArtistCardSourceLogo(artistCard: Card): String
}

private const val DB_SAVED_SYMBOL = "[*]"
private const val NO_RESULTS = "No Results"

internal class ArtistCardHelperImpl(private val htmlHelper: HtmlHelper): ArtistCardHelper {

    override fun getArtistCardDescription(artistName: String, artistCard: Card): String {
        return artistCard.formatDescription(artistName)
    }

    private fun Card.formatDescription(artistName: String): String {
        val dbSaved = if (isLocallyStored) DB_SAVED_SYMBOL else ""
        val descriptionFormatted = if (description.isEmpty()) NO_RESULTS else htmlHelper.getHtmlText(description, artistName)

        return dbSaved + descriptionFormatted
    }

    override fun getArtistCardInfoUrl(artistCard: Card): String {
        return artistCard.infoUrl
    }

    override fun getArtistCardSource(artistCard: Card): String {
        return artistCard.source.name
    }

    override fun getArtistCardSourceLogo(artistCard: Card): String {
        return artistCard.sourceLogo
    }

}