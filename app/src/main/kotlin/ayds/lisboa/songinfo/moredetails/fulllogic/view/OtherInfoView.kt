package ayds.lisboa.songinfo.moredetails.fulllogic.view

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
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfo

private const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

internal class OtherInfoView : AppCompatActivity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var openUrlButton: View

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initView()
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

    private fun open() {
        Thread {
            // SOLO DEBE SER UN SET, NO GET + SET
            // POSIBLEMENTE SEA SOLO EL UPDATEVIEW()
            getArtistInfoOnUpdateView()
        }.start()
    }

    private fun getArtistInfoOnUpdateView() {
        // MOVER METODO COMPLETO AL PRESENTER -> notify() por observer
        val artistName = getArtistName()
        val artistInfo = getArtistInfo(artistName)
        updateView(artistName, artistInfo)
    }

    private fun getArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
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
        val bioContentFormatted = getArtistInfoText(artistName, artistInfo) // MOVER ESTE GET AL PRESENTER

        artistInfoTextView.text = HtmlCompat.fromHtml(bioContentFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setURL(artistInfo: ArtistInfo) {
        val url = getArtistInfoUrl(artistInfo) // MOVER ESTE GET AL PRESENTER

        openUrlButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

}