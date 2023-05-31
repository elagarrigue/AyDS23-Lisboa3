package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard

private const val DB_SAVED_SYMBOL = "[*]"
private const val LINE_BREAK = "<br/>"

interface ArtistCardHelper {
    fun getArtistCards(artistName: String, artistCards: List<ArtistCard>): List<ArtistCardState>
}

internal class ArtistCardHelperImpl(private val htmlHelper: HtmlHelper, private val sourceFactory: SourceFactory) : ArtistCardHelper {
    override fun getArtistCards(artistName: String, artistCards: List<ArtistCard>): List<ArtistCardState> {
        val artistCardState: MutableList<ArtistCardState> = mutableListOf()

        artistCards.forEach {
            artistCardState.add(
                ArtistCardState(
                    formattedDescription = it.formatDescription(artistName),
                    infoUrl = it.infoUrl,
                    title = it.formatSource(),
                    sourceLogo = it.sourceLogo

                )
            )
        }

        return artistCardState
    }

    private fun ArtistCard.formatDescription(artistName: String): String {
        val dbSaved = if (isLocallyStored) "$DB_SAVED_SYMBOL $LINE_BREAK" else ""
        val descriptionFormatted = htmlHelper.getHtmlText(description, artistName)

        return dbSaved + descriptionFormatted
    }

    private fun ArtistCard.formatSource(): String{
        return sourceFactory.get(source)
    }
}
