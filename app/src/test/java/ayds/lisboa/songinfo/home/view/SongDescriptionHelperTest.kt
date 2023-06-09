package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class SongDescriptionHelperTest {

    private val dateCalculator = mockk<DateCalculator>()
    private val dateCalculatorFactory = mockk<DateCalculatorFactory>()
    private val songDescriptionHelper by lazy { SongDescriptionHelperImpl(dateCalculatorFactory) }

    @Test
    fun `given a local song it should return the description`() {
        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "day",
            "url",
            "url",
            true,
        )
        every { dateCalculatorFactory.get("1992-01-01", "day") } returns dateCalculator
        every { dateCalculator.getDate() } returns "01/01/1992"

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush [*]\n" +
                "Artist: Stone Temple Pilots\n" +
                "Album: Core\n" +
                "Release date: 01/01/1992"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non local song it should return the description`() {
        val song: Song = SpotifySong(
            "id",
            "Plush",
            "Stone Temple Pilots",
            "Core",
            "1992-01-01",
            "day",
            "url",
            "url",
            false,
        )
        every { dateCalculatorFactory.get("1992-01-01", "day") } returns dateCalculator
        every { dateCalculator.getDate() } returns "01/01/1992"

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected =
            "Song: Plush \n" +
                "Artist: Stone Temple Pilots\n" +
                "Album: Core\n" +
                "Release date: 01/01/1992"

        assertEquals(expected, result)
    }

    @Test
    fun `given a non spotify song it should return the song not found description`() {
        val song: Song = mockk()

        val result = songDescriptionHelper.getSongDescriptionText(song)

        val expected = "Song not found"

        assertEquals(expected, result)
    }
}