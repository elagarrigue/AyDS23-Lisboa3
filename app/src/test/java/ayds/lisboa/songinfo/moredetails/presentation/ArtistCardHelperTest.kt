package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

private const val DB_SAVED_SYMBOL = "[*]"
private const val LINE_BREAK = "<br/>"

internal class ArtistCardHelperTest {

    private val htmlHelper = mockk<HtmlHelper>()
    private val sourceFactory = mockk<SourceFactory>()
    private val artistCardHelper: ArtistCardHelper by lazy {
        ArtistCardHelperImpl(htmlHelper, sourceFactory)
    }

    @Test
    fun `given an artistName and cards with not empty description and locallyStored it should return the right formatted information`() {
        val artistName = "artistName"
        val artistCards = listOf(
            ArtistCard("description", "infoUrl", Source.LastFm, "sourceLogo", true),
            ArtistCard("description", "infoUrl", Source.NewYorkTimes, "sourceLogo", true),
            ArtistCard("description", "infoUrl", Source.Wikipedia, "sourceLogo", true)
        )

        every { htmlHelper.getHtmlText("description", "artistName") } returns "formattedDescription"
        every { sourceFactory.get(any()) } returns "title"

        val result = artistCardHelper.getArtistCards(artistName, artistCards)

        result.forEach {
            assertEquals("$DB_SAVED_SYMBOL $LINE_BREAK" + "formattedDescription", it.formattedDescription)
            assertEquals("infoUrl", it.infoUrl)
            assertEquals("title", it.title)
            assertEquals("sourceLogo", it.sourceLogo)
        }
    }

    @Test
    fun `given an artistName and cards with not empty description and not locallyStored it should return the right formatted information`() {
        val artistName = "artistName"
        val artistCards = listOf(
            ArtistCard("description", "infoUrl", Source.LastFm, "sourceLogo", false),
            ArtistCard("description", "infoUrl", Source.NewYorkTimes, "sourceLogo", false),
            ArtistCard("description", "infoUrl", Source.Wikipedia, "sourceLogo", false)
        )

        every { htmlHelper.getHtmlText("description", "artistName") } returns "formattedDescription"
        every { sourceFactory.get(any()) } returns "title"

        val result = artistCardHelper.getArtistCards(artistName, artistCards)

        result.forEach {
            assertEquals("formattedDescription", it.formattedDescription)
            assertEquals("infoUrl", it.infoUrl)
            assertEquals("title", it.title)
            assertEquals("sourceLogo", it.sourceLogo)
        }
    }

}