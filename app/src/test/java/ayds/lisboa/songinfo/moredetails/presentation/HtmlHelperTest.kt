import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelper
import ayds.lisboa.songinfo.moredetails.presentation.HtmlHelperImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class HtmlHelperImplTest {

    private val htmlHelper: HtmlHelper by lazy {
        HtmlHelperImpl()
    }
    @Test
    fun `getHtmlText should return the correct HTML text`() {

        val text = "Esto es una frase de testing"
        val term = "frase"

        val expectedText = """<html><div width=400><font face="arial">Esto es una frase de testing</font></div></html>""".trimIndent()

        val result = htmlHelper.getHtmlText(text, term)

        assertEquals(expectedText, result)
    }
}