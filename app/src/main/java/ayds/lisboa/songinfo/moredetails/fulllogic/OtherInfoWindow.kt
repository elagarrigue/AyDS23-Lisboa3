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
import ayds.lisboa.songinfo.home.model.entities.Song
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.SpotifyArtistInfo

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

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var openUrlButton: View
    private lateinit var dataBase: DataBase
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
        dataBase = DataBase(this)
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

        if (artistInfo.hasContent()) {
            artistInfo.bioContent = NO_RESULTS
        } else {
            artistInfo.bioContent = jsonTextToHtml(artistInfo.bioContent, artistName)
        }

        updateView(artistInfo)
    }

    private fun getArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun getArtistInfo(artistName: String): SpotifyArtistInfo? {
        var artistInfo: SpotifyArtistInfo? = getArtistInfoFromDb(artistName)

        when {
            artistInfo != null -> markArtistInfoAsSavedDB(artistInfo)
            else -> {
                try {
                    artistInfo = getArtistInfoFromService(artistName)

                    if (artistInfo.isLocallyStored) {
                        saveArtistInfoDB(artistName, artistInfo)
                    }

                } catch (ioException: Exception) {
                    ioException.printStackTrace()
                }
            }
        }

        return artistInfo
    }

    private fun SpotifyArtistInfo.hasContent() = (bioContent.isNotEmpty())

    private fun getArtistInfoFromDb(artistName: ArtistInfo): SpotifyArtistInfo {
        return dataBase.getInfo(artistName)
    }

    private fun markArtistInfoAsSavedDB(artistInfo: SpotifyArtistInfo) {
        artistInfo.isLocallyStored = true
    }

    private fun getArtistInfoFromService(artistName: String): SpotifyArtistInfo {
        val bioContent = getBioContent(artistName)
        val url = getArtistInfoUrl(artistName)
        val isLocallyStored = false

        return SpotifyArtistInfo(bioContent, url, isLocallyStored)
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

    private fun saveArtistInfoDB(artistName: String, artistInfo: SpotifyArtistInfo) {
        dataBase.saveArtist(artistName, artistInfo)
    }

    private fun updateView(artistInfo: SpotifyArtistInfo) {
        runOnUiThread {
            setDefaultImage()
            setArtistInfo(artistInfo)
            setURL(artistInfo)
        }
    }

    private fun setDefaultImage() {
        Picasso.get().load(LAST_FM_DEFAULT_IMAGE).into(imageView)
    }

    private fun setArtistInfo(artistInfo: SpotifyArtistInfo) {
        artistInfoTextView.text = HtmlCompat.fromHtml(artistInfo.bioContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setURL(artistInfo: SpotifyArtistInfo) {
        openUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(artistInfo.url)
            startActivity(intent)
        }
    }

}