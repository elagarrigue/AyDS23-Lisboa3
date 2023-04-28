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
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.EmptyArtistInfo

private const val JSON_ARTIST = "artist"
private const val JSON_BIO = "bio"
private const val JSON_CONTENT = "content"
private const val JSON_URL = "url"
private const val LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val HTML_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_FINALS = "</font></div></html>"
private const val NO_RESULTS = "No Results"

internal class OtherInfoWindow : AppCompatActivity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var openUrlButton: View
    private lateinit var dataBase: Database
    private lateinit var retrofit: Retrofit
    private lateinit var lastFMAPI: LastFMAPI

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initView()
        initDatabase()
        initRetrofit()
        initLastFmApi()
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
        dataBase = DataBaseImpl(this)
    }

    private fun initRetrofit() {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.baseUrl(LAST_FM_API_BASE_URL)
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
        retrofit = retrofitBuilder.build()
    }

    private fun initLastFmApi() {
        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun open() {
        Thread {
            getArtistInfoOnUpdateView()
        }.start()
    }

    private fun getArtistInfoOnUpdateView() {
        val artistName = getArtistName()
        val artistInfo = getArtistInfo(artistName)
        updateView(artistName, artistInfo as LastFmArtistInfo)
    }

    private fun getArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun getArtistInfo(artistName: String): ArtistInfo {
        var artistInfo = getArtistInfoFromDb(artistName)

        when {
            artistInfo != null -> markArtistInfoAsSavedDB(artistInfo)
            else -> {
                try {
                    artistInfo = getArtistInfoFromService(artistName)
                    saveArtistInfoDB(artistName, artistInfo)
                    markArtistInfoAsSavedDB(artistInfo)
                } catch (ioException: Exception) {
                    ioException.printStackTrace()
                }
            }
        }

        return artistInfo ?: EmptyArtistInfo
    }

    private fun getArtistInfoFromDb(artistName: String): LastFmArtistInfo? {
        return dataBase.getArtistInfo(artistName)
    }
    
    private fun markArtistInfoAsSavedDB(artistInfo: LastFmArtistInfo) {
        artistInfo.isLocallyStored = true
    }
    
    private fun getArtistInfoFromService(artistName: String): LastFmArtistInfo {
        val bioContent = getBioContent(artistName)
        val url = getArtistInfoUrl(artistName)

        return LastFmArtistInfo(bioContent, url)
    }

    private fun getBioContent(artistName: String): String {
        val callResponseJson = getJsonResponse(artistName)
        val artist = callResponseJson[JSON_ARTIST].asJsonObject
        val bio = artist[JSON_BIO].asJsonObject
        val bioContent = bio[JSON_CONTENT]

        return bioContent.asString
    }

    private fun getArtistInfoUrl(artistName: String): String {
        val callResponseJson = getJsonResponse(artistName)
        val artist = callResponseJson[JSON_ARTIST].asJsonObject
        val url = artist[JSON_URL]

        return url.asString
    }

    private fun getJsonResponse(artistName: String): JsonObject {
        val callResponse = lastFMAPI.getArtistInfo(artistName).execute()

        return Gson().fromJson(callResponse.body(), JsonObject::class.java)
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

    private fun saveArtistInfoDB(artistName: String, artistInfo: LastFmArtistInfo) {
        dataBase.saveArtist(artistName, artistInfo)
    }

    private fun updateView(artistName: String, artistInfo: LastFmArtistInfo) {
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
        val bioContentFormatted = formatBioContent(artistName, artistInfo as LastFmArtistInfo)
        artistInfoTextView.text = HtmlCompat.fromHtml(bioContentFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun formatBioContent(artistName: String, artistInfo: LastFmArtistInfo): String {
        val bioContent = artistInfo.bioContent
        return if (bioContent.isEmpty()) NO_RESULTS else jsonTextToHtml(bioContent, artistName)
    }

    private fun setURL(artistInfo: LastFmArtistInfo) {
        openUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(artistInfo.url)
            startActivity(intent)
        }
    }

}