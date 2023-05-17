package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ArtistInfoHelperTest {

    private val htmlHelper = mockk<HtmlHelper>()
    private val artistInfoHelper by lazy {ArtistInfoHelperImpl(htmlHelper)}


    @Test
    fun `given an artistName and a lastFMArtistInfo with not empty bio and locallyStored it should return the right formatted text`() {
        val artistName = ""
        val artistInfo: ArtistInfo = ArtistInfo.LastFmArtistInfo(
            "bioContent",
            "url",
            true
        )

        every { htmlHelper.getHtmlText("bioContent", "") } returns "Formatted bio"
        val result = artistInfoHelper.getArtistInfoText(artistName, artistInfo)

        assertEquals("[*]Formatted bio", result)
    }

    @Test
    fun `given an artistName and a lastFMArtistInfo with not empty bio and not locallyStored it should return the right formatted text`() {
        val artistName = ""
        val artistInfo: ArtistInfo = ArtistInfo.LastFmArtistInfo(
            "bioContent",
            "url",
            false
        )

        every { htmlHelper.getHtmlText("bioContent", "") } returns "Formatted bio"
        val result = artistInfoHelper.getArtistInfoText(artistName, artistInfo)

        assertEquals("Formatted bio", result)
    }

    @Test
    fun `given an artistName and a lastFMArtistInfo with empty bio and locallyStored it should return the right formatted text`() {
        val artistName = ""
        val artistInfo: ArtistInfo = ArtistInfo.LastFmArtistInfo(
            "",
            "url",
            true
        )

        val result = artistInfoHelper.getArtistInfoText(artistName, artistInfo)

        assertEquals("[*]No Results", result)
    }

    @Test
    fun `given an artistName and a lastFMArtistInfo with empty bio and not locallyStored it should return the right formatted text`() {
        val artistName = ""
        val artistInfo: ArtistInfo = ArtistInfo.LastFmArtistInfo(
            "",
            "url",
            false
        )

        val result = artistInfoHelper.getArtistInfoText(artistName, artistInfo)

        assertEquals("No Results", result)
    }

    @Test
    fun `given a non last fm artist info return not found`(){
        val artistName=""
        val artistInfo: ArtistInfo = mockk()

        val result = artistInfoHelper.getArtistInfoText(artistName,artistInfo)
        val expected = "Artist info not found."

        assertEquals(expected, result)
    }

    @Test
    fun `given an artist info it should return the url`() {
        val artistInfo = ArtistInfo.LastFmArtistInfo("bioContent", "url", false)
        val expectedUrl = "url"

        val result = artistInfoHelper.getArtistInfoUrl(artistInfo)

        assertEquals(expectedUrl, result)
    }

    @Test
    fun `given an artist info with invalid url it should return the no url found message`() {
        val artistInfo = ArtistInfo.LastFmArtistInfo("bioContent", "", false)
        val expectedUrl = ""

        val result = artistInfoHelper.getArtistInfoUrl(artistInfo)

        assertEquals(expectedUrl, result)
    }

    @Test
    fun `given an artist info that is not LastFmArtistInfo it should return the no url found message`() {
        val artistInfo = ArtistInfo.EmptyArtistInfo
        val expectedUrl = "Artist info url not found."
        val result = artistInfoHelper.getArtistInfoUrl(artistInfo)

        assertEquals(expectedUrl, result)
    }



}