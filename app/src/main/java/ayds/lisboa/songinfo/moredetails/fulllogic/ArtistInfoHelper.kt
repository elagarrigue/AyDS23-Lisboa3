package ayds.lisboa.songinfo.moredetails.fulllogic

interface ArtistInfoHelper {

    fun getArtistInfoText(artistName: String, artistInfo: ArtistInfo): String

    fun getArtistInfoUrl(artistInfo: ArtistInfo): String

}

private const val DB_SAVED_SYMBOL = "[*]"
private const val NO_RESULTS = "No Results"
private const val NO_ARTIST_INFO_FOUND = "Artist info not found."
private const val NO_ARTIST_INFO_URL_FOUND = "Artist info url not found."

internal class ArtistInfoHelperImpl(val htmlHelper: HtmlHelper): ArtistInfoHelper {

    override fun getArtistInfoText(artistName: String, artistInfo: ArtistInfo): String {
        return when (artistInfo) {
            is ArtistInfo.LastFmArtistInfo -> artistInfo.formatBioContent(artistName)
            else -> NO_ARTIST_INFO_FOUND
        }
    }

    private fun ArtistInfo.LastFmArtistInfo.formatBioContent(artistName: String): String {
        val dbSaved = if (isLocallyStored) DB_SAVED_SYMBOL else ""
        val bioContentFormatted =
            if (bioContent.isEmpty()) NO_RESULTS else htmlHelper.jsonTextToHtml(bioContent, artistName)

        return dbSaved + bioContentFormatted
    }

    override fun getArtistInfoUrl(artistInfo: ArtistInfo): String {
        return when (artistInfo) {
            is ArtistInfo.LastFmArtistInfo -> artistInfo.url
            else -> NO_ARTIST_INFO_URL_FOUND
        }
    }

}