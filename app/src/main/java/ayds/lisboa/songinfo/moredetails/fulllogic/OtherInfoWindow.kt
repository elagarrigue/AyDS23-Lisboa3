package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import com.squareup.picasso.Picasso
import java.util.*
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.EmptyArtistInfo
import retrofit2.Response

private const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val DB_SAVED_SYMBOL = "[*]"
private const val HTML_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_FINALS = "</font></div></html>"
private const val NO_RESULTS = "No Results"
private const val NO_ARTIST_INFO_FOUND = "Artist info not found."
private const val NO_ARTIST_INFO_URL_FOUND = "Artist info url not found."

internal class OtherInfoWindow : AppCompatActivity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var openUrlButton: View
    private lateinit var dataBase: DataBase
    private lateinit var lastFMService: LastFmService
    private lateinit var lastFmToArtistInfoResolver: LastFmToArtistInfoResolver

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initView()
        initDatabase()
        initLastFmService()
        initLastFmToArtistInfoResolver()
        open()
    }

    private fun initContentView() {
        setContentView(R.layout.activity_other_info)
    }

    private fun initView() {
        artistInfoTextView = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)

    }

    private fun initDatabase() {
        // INICIALIZAR CURSOR EN INJECTOR
        dataBase = LastFMLocalStorageImpl(this, CursorToLastFMArtistMapperImpl())
    }

    private fun initLastFmService() {
        lastFMService = LastFmServiceImpl()
    }

    private fun initLastFmToArtistInfoResolver() {
        lastFmToArtistInfoResolver = LastFmToArtistInfoResolverImpl()
    }

    private fun open() {
        Thread {
            getArtistInfoOnUpdateView()
        }.start()
    }

    private fun getArtistInfoOnUpdateView() {
        val artistName = getArtistName()
        val artistInfo = getArtistInfo(artistName)
        updateView(artistName, artistInfo)
    }

    private fun getArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun getArtistInfoFromDb(artistName: String): LastFmArtistInfo? {
        return dataBase.getArtistInfo(artistName)
    }
    


    private fun getArtistInfoFromService(artistName: String): ArtistInfo? {
        val artist = getResponse(artistName)
        return lastFmToArtistInfoResolver.getArtistInfoFromExternalData(artist.body())
    }

    private fun getResponse(artistName: String): Response<String> {
        return lastFMService.getResponse(artistName)
    }

    private fun updateView(artistName: String, artistInfo: ArtistInfo) {
        runOnUiThread {
            setDefaultImage()
            setBioContent(artistName, artistInfo)
            setURL(artistInfo)
        }
    }

    private fun setDefaultImage() {
        Picasso.get().load(LAST_FM_DEFAULT_IMAGE).into(imageView)
    }

    private fun setBioContent(artistName: String, artistInfo: ArtistInfo) {
        val bioContentFormatted = getArtistInfoText(artistName, artistInfo)
        artistInfoTextView.text = HtmlCompat.fromHtml(bioContentFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun getArtistInfoText(artistName: String, artistInfo: ArtistInfo): String {
        return when (artistInfo) {
            is LastFmArtistInfo -> artistInfo.formatBioContent(artistName)
            else -> NO_ARTIST_INFO_FOUND
        }
    }

    private fun LastFmArtistInfo.formatBioContent(artistName: String): String {
        val dbSaved = if (isLocallyStored) DB_SAVED_SYMBOL else ""
        val bioContentFormatted = if (bioContent.isEmpty()) NO_RESULTS else jsonTextToHtml(bioContent, artistName)

        return dbSaved + bioContentFormatted
    }

    private fun jsonTextToHtml(text: String, term: String): String {
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

    private fun setURL(artistInfo: ArtistInfo) {
        val url = getArtistInfoUrl(artistInfo)

        openUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun getArtistInfoUrl(artistInfo: ArtistInfo): String {
        return when (artistInfo) {
            is LastFmArtistInfo -> artistInfo.url
            else -> NO_ARTIST_INFO_URL_FOUND
        }
    }

}