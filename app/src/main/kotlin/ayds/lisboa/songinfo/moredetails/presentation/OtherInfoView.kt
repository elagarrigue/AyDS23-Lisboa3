package ayds.lisboa.songinfo.moredetails.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.dependencyinyector.MoreDetailsInjector
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.navigation.NavigationUtils
import ayds.lisboa.songinfo.utils.view.ImageLoader
import ayds.observer.Observer

class OtherInfoView: AppCompatActivity() {
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader

    private lateinit var artistInfoTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var openUrlButton: View

    private lateinit var presenter: OtherInfoPresenter

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initModule()
        initProperties()
        subscribeToPresenter()
        open()
    }

    private fun getArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun initModule() {
        MoreDetailsInjector.init(this)
    }

    private fun initContentView() {
        setContentView(R.layout.activity_other_info)
    }

    private fun initProperties() {
        artistInfoTextView = findViewById(R.id.otherInfoTextView)
        imageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)

    }

    private fun subscribeToPresenter() {
        presenter = MoreDetailsInjector.getPresenter()
        presenter.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiState> =
        Observer { value -> updateView(value) }

    private fun updateView(uiState: OtherInfoUiState) {
        runOnUiThread {
            setDescription(uiState.artistCardDescription)
            setInfoUrl(uiState.artistCardInfoUrl)
            setSourceLogo(uiState.artistCardSourceLogo)
        }
    }

    private fun setDescription(artistInfoBioContent: String) {
        artistInfoTextView.text = HtmlCompat.fromHtml(artistInfoBioContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setInfoUrl(artistInfoUrl: String) {
        openUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, artistInfoUrl) }
    }

    private fun setSourceLogo(artistCardSourceLogo: String) {
        imageLoader.loadImageIntoView(artistCardSourceLogo, imageView)
    }

    private fun open() {
        presenter.fetch(getArtistName())
    }

}