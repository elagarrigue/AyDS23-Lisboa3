package ayds.lisboa.songinfo.moredetails.fulllogic.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.fulllogic.MoreDetailsInjector
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfo
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfo.LastFmArtistInfo
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfo.EmptyArtistInfo
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.navigation.NavigationUtils
import ayds.lisboa.songinfo.utils.view.ImageLoader

interface OtherInfoView{
    var uiState: OtherInfoUiState

    fun getArtistName():String
    fun updateView(artistInfo: ArtistInfo)

}
class OtherInfoViewActivity() : AppCompatActivity(), OtherInfoView {
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader

    private lateinit var artistInfoTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var openUrlButton: View

    override var uiState: OtherInfoUiState = OtherInfoUiState()

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initModule()
        initProperties()
    }

    private fun initModule() {
        MoreDetailsInjector.init(this)
    }

    private fun initContentView() {
        setContentView(R.layout.activity_other_info)
    }

    private fun initProperties() {
        artistInfoTextView = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)

    }

    override fun getArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    override fun updateView(artistInfo: ArtistInfo) {
        runOnUiThread {
            updateUiState(artistInfo)
            setDefaultImage()
            setBioContent()
            setUrl()
        }
    }

    private fun updateUiState(artistInfo: ArtistInfo) {
        when (artistInfo) {
            is LastFmArtistInfo -> updateOtherInfoState(artistInfo)
            EmptyArtistInfo -> updateNoResultsUiState()
        }
    }

    private fun updateOtherInfoState(artistInfo: LastFmArtistInfo) {
        uiState.copy(
            artistInfoBioContent = artistInfo.bioContent,
            artistInfoUrl = artistInfo.url
        )
    }

    private fun updateNoResultsUiState() {
        uiState.copy(
            artistInfoBioContent = "NO RESULTS",
            artistInfoUrl = ""
        )
    }

    private fun setDefaultImage() {
        imageLoader.loadImageIntoView(uiState.lastFmDefaultImage, imageView);
    }

    private fun setBioContent() {
        artistInfoTextView.text = HtmlCompat.fromHtml(uiState.artistInfoBioContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setUrl() {
        openUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, uiState.artistInfoUrl) }
    }

}