package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface ArtistCardHelper {
    fun getArtistCards(artistName: String, artistCards: List<ArtistCard>): List<ArtistCardState>
}

private const val DB_SAVED_SYMBOL = "[*]"
private const val NO_RESULTS = "No Results"

internal class ArtistCardHelperImpl(private val htmlHelper: HtmlHelper, private val sourceFactory: SourceFactory) : ArtistCardHelper {

    override fun getArtistCards(artistName: String, artistCards: List<ArtistCard>): List<ArtistCardState> {
        val artistCardState: MutableList<ArtistCardState> = mutableListOf()
        artistCards.forEach {
            artistCardState.add(
                ArtistCardState(
                    descriptionFormatted = formatDescription(it.description, artistName),
                    infoUrl = it.infoUrl,
                    title = formatSource(it.source),
                    sourceLogo = it.sourceLogo

                )
            )
        }

        return artistCardState
    }

    private fun formatDescription(description: String, artistName: String): String {
        val dbSaved = if (description.isNotEmpty()) DB_SAVED_SYMBOL else ""
        val descriptionFormatted = if (description.isEmpty()) NO_RESULTS else htmlHelper.getHtmlText(description, artistName)

        return dbSaved + descriptionFormatted
    }

    private fun formatSource(source: Source): String{
        return sourceFactory.get(source)
    }
}
