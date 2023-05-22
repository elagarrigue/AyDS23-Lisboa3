package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CardHelperTest {

    private val htmlHelper = mockk<HtmlHelper>()
    private val artistInfoHelper by lazy {ArtistCardHelperImpl(htmlHelper)}


    @Test
    fun `given an artistName and a lastFMArtistInfo with not empty bio and locallyStored it should return the right formatted text`() {
        val artistName = ""
        val card: Card = Card.ArtistCard(
            "bioContent",
            "url",
            true
        )

        every { htmlHelper.getHtmlText("bioContent", "") } returns "Formatted bio"
        val result = artistInfoHelper.getArtistCardDescription(artistName, card)

        assertEquals("[*]Formatted bio", result)
    }

    @Test
    fun `given an artistName and a lastFMArtistInfo with not empty bio and not locallyStored it should return the right formatted text`() {
        val artistName = ""
        val card: Card = Card.ArtistCard(
            "bioContent",
            "url",
            false
        )

        every { htmlHelper.getHtmlText("bioContent", "") } returns "Formatted bio"
        val result = artistInfoHelper.getArtistCardDescription(artistName, card)

        assertEquals("Formatted bio", result)
    }

    @Test
    fun `given an artistName and a lastFMArtistInfo with empty bio and locallyStored it should return the right formatted text`() {
        val artistName = ""
        val card: Card = Card.ArtistCard(
            "",
            "url",
            true
        )

        val result = artistInfoHelper.getArtistCardDescription(artistName, card)

        assertEquals("[*]No Results", result)
    }

    @Test
    fun `given an artistName and a lastFMArtistInfo with empty bio and not locallyStored it should return the right formatted text`() {
        val artistName = ""
        val card: Card = Card.ArtistCard(
            "",
            "url",
            false
        )

        val result = artistInfoHelper.getArtistCardDescription(artistName, card)

        assertEquals("No Results", result)
    }

    @Test
    fun `given a non lastFMArtistInfo info return not found`(){
        val artistName=""
        val card: Card = mockk()

        val result = artistInfoHelper.getArtistCardDescription(artistName,card)
        val expected = "Artist info not found."

        assertEquals(expected, result)
    }

    @Test
    fun `given a lastFMArtistInfo it should return the url`() {
        val card = Card.ArtistCard("bioContent", "url", false)
        val expectedUrl = "url"

        val result = artistInfoHelper.getArtistCardInfoUrl(card)

        assertEquals(expectedUrl, result)
    }

    @Test
    fun `given a non lastFMArtistInfo it should return the no url found message`() {
        val card = Card.EmptyCard
        val expectedUrl = "Artist info url not found."
        val result = artistInfoHelper.getArtistCardInfoUrl(card)

        assertEquals(expectedUrl, result)
    }



}