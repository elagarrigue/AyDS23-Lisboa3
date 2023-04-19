package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

private const val ARTIST_NAME_EXTRA = "artistName"
private const val TAG = "tag"
private const val JSON = "JSON"
private const val JSON_ARTIST = "artist"
private const val JSON_BIO = "bio"
private const val JSON_CONTENT = "content"
private const val JSON_URL = "url"
private const val LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/"
private const val DB_SAVE_SYMBOL = "[*]"
private const val LAST_FM_DEFAULT_IMAGE =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val HTML_WIDTH = "<html><div width=400>"
private const val HTML_FONT = "<font face=\"arial\">"
private const val HTML_FINALS = "</font></div></html>"
private const val NO_RESULTS = "No Results"

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var artistInfo: String
    private lateinit var artistName: String
    private lateinit var view: TextView
    private lateinit var retrofit: Retrofit
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var dataBase: DataBase

    fun getArtistNameExtra() = ARTIST_NAME_EXTRA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initView()
        initDatabase()
        initRetrofit()
        initLastFMAPI()
        open()
    }

    private fun initContentView() {
        setContentView(R.layout.activity_other_info)
    }

    private fun initView() {
        view = findViewById(R.id.textPane2)
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

    private fun initLastFMAPI() {
        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun getArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun getArtistInfo() {
        val space = " "
        Log.e(TAG, "$ARTIST_NAME_EXTRA + $space + $artistName")
        Thread {
            getArtistInfoDB()
            if (existArtistInfoDB()) {
                markArtistInfoAsSavedDB()
            } else {
                getFromService()
            }
            update()
        }.start()
    }

    private fun open() {
        getArtistName()
        getArtistInfo()
    }

    private fun getArtistInfoDB() {
        artistInfo = dataBase.getInfo(artistName)
    }

    private fun existArtistInfoDB(): Boolean {
        return artistInfo.isEmpty()
    }

    private fun markArtistInfoAsSavedDB() {
        artistInfo = DB_SAVE_SYMBOL + artistInfo
    }

    private fun getFromService() {
        val callResponseJson: JsonObject
        try {
            callResponseJson = getJSONResponse()
            setBioContent(callResponseJson)
            setURL(callResponseJson)
        } catch (ioException: IOException) {
            Log.e(TAG, "Error $ioException")
            ioException.printStackTrace()
        }
    }

    private fun setBioContent(callResponseJson: JsonObject) {
        val bioContent = getBioContent(callResponseJson)

        if (bioContent.isEmpty()) {
            artistInfo = NO_RESULTS
        } else {
            val lineBreakJSON = "\\n"
            val lineBreak = "\n"
            artistInfo = bioContent.replace(lineBreakJSON, lineBreak)
            artistInfo = textToHtml(artistInfo, artistName)
            saveArtistInfoDB()
        }
    }

    private fun setURL(callResponseJson: JsonObject) {
        val url = getURL(callResponseJson)
        urlAction(url)
    }

    private fun urlAction(urlString: String) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun getJSONResponse(): JsonObject {
        val callResponse = lastFMAPI.getArtistInfo(artistName).execute()
        val gson = Gson()
        val result = gson.fromJson(callResponse.body(), JsonObject::class.java)
        Log.e(TAG, JSON + " " + callResponse.body())
        return result
    }

    private fun getBioContent(callResponseJson: JsonObject): String {
        val artist = callResponseJson[JSON_ARTIST].asJsonObject
        val bio = artist[JSON_BIO].asJsonObject
        val bioContent = bio[JSON_CONTENT]
        return bioContent.asString
    }

    private fun getURL(callResponseJson: JsonObject): String {
        val artist = callResponseJson[JSON_ARTIST].asJsonObject
        val url = artist[JSON_URL]
        return url.asString
    }

    private fun saveArtistInfoDB() {
        dataBase.saveArtist(artistName, artistInfo)
    }

    private fun update() {
        val textGetImageFrom ="Get Image From"
        Log.e(TAG, "$textGetImageFrom, $LAST_FM_DEFAULT_IMAGE")
        runOnUiThread {
            setDefaultImage()
            setArtistInfo()
        }
    }

    private fun setDefaultImage() {
        val imageView = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(LAST_FM_DEFAULT_IMAGE).into(imageView)
    }

    private fun setArtistInfo() {
        view.text = HtmlCompat.fromHtml(artistInfo, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append(HTML_WIDTH)
        builder.append(HTML_FONT)
        builder.append(getTextWithBold(text, term))
        builder.append(HTML_FINALS)
        return builder.toString()
    }

    private fun getTextWithBold(text: String, term: String?): String {
        val singleQuote = "'"
        val space = " "
        val lineBreak = "\n"
        val lineBreakHTML = "<br>"
        val caseInsensitive = "(?i)"
        val startBoldLabel = "<b>"
        val finishBoldLabel = "</b>"
        text.replace(singleQuote, space)
        text.replace(lineBreak, lineBreakHTML)
        text.replace(
            (caseInsensitive + term).toRegex(),
            startBoldLabel + term!!.uppercase(Locale.getDefault()) + finishBoldLabel
        )
        return text
    }
}