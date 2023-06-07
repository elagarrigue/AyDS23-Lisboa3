package ayds.lisboa.songinfo.moredetails.presentation

import org.junit.Assert.assertEquals
import org.junit.Test

internal class HtmlHelperTest {

    private val htmlHelper: HtmlHelper by lazy { HtmlHelperImpl() }

    @Test
    fun `getHtmlText should return the correct HTML text`() {
        val text = "Hello world Lisbon 3"
        val term = "world"

        val expectedHtmlText = "<html><div width=400><font face=\"arial\">Hello <b>WORLD</b> Lisbon 3</font></div></html>"
        val result = htmlHelper.getHtmlText(text, term)

        assertEquals(expectedHtmlText, result)
    }

}
