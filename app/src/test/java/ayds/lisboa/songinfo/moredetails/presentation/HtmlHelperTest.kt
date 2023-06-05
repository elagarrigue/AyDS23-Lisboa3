package ayds.lisboa.songinfo.moredetails.presentation

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

internal class HtmlHelperTest {

    private val htmlHelper = mockk<HtmlHelper>()

    @Test
    fun `getHtmlText should return the correct HTML text`() {
        val text = "Hello world Lisbon 3"
        val term = "world"
        val expectedHtmlText = "<html><div width=400><font face=\"arial\">Hello <b>WORLD</b> Lisbon 3</font></div></html>"

        every { htmlHelper.getHtmlText(text, term) } returns expectedHtmlText
        val result = htmlHelper.getHtmlText(text, term)

        assertEquals(expectedHtmlText, result)
    }

}
