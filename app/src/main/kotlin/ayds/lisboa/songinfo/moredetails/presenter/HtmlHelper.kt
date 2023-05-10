package ayds.lisboa.songinfo.moredetails.presenter

import java.util.Locale

private const val HTML_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_FINALS = "</font></div></html>"

interface HtmlHelper {
    fun getHtmlText(text: String, term: String): String
}

class HtmlHelperImpl: HtmlHelper {

    override fun getHtmlText(text: String, term: String): String {
        val builder = StringBuilder()
        val textWithBold = getBoldHtmlText(text, term)

        builder.append(HTML_WIDTH)
        builder.append(HTML_FONT)
        builder.append(textWithBold)
        builder.append(HTML_FINALS)

        return builder.toString()
    }

    private fun getBoldHtmlText(text: String, term: String): String {
        val singleQuote = "'"
        val space = " "
        val lineBreakJson = "\\n"
        val lineBreakHTML = "<br>"
        val caseInsensitive = "(?i)"
        val startBoldLabel = "<b>"
        val finishBoldLabel = "</b>"

        text.replace(singleQuote, space)
        text.replace(lineBreakJson, lineBreakHTML)
        text.replace((caseInsensitive + term).toRegex(),
            startBoldLabel + term.uppercase(Locale.getDefault()) + finishBoldLabel)

        return text
    }

}