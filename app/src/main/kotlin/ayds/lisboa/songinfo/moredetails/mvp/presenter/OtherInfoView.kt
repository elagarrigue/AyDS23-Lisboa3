package ayds.lisboa.songinfo.moredetails.mvp.presenter

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.mvp.injector.MoreDetailsInjector
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.navigation.NavigationUtils
import ayds.lisboa.songinfo.utils.view.ImageLoader
import ayds.observer.Observer

interface OtherInfoView{
    fun getArtistName():String
}

class OtherInfoViewActivity : AppCompatActivity(), OtherInfoView {
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
        subscribeToPresenter()
        initProperties()
        open()
    }

    private fun open() {
        presenter.fetch(getArtistName())
    }

    override fun getArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun initModule() {
        MoreDetailsInjector.init(this)
    }

    private fun subscribeToPresenter() {
        presenter = MoreDetailsInjector.getPresenter()
        presenter.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiState> =
        Observer { value -> updateView(value) }

    private fun initContentView() {
        setContentView(R.layout.activity_other_info)
    }

    private fun initProperties() {
        artistInfoTextView = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)

    }

    private fun updateView(uiState: OtherInfoUiState) {
        runOnUiThread {
            setDefaultImage(uiState.lastFmDefaultImage)
            setBioContent(uiState.artistInfoBioContent)
            setUrl(uiState.artistInfoUrl)
        }
    }

    private fun setDefaultImage(lastFmDefaultImage: String) {
        imageLoader.loadImageIntoView(lastFmDefaultImage, imageView)
    }

    private fun setBioContent(artistInfoBioContent: String) {
        artistInfoTextView.text = HtmlCompat.fromHtml(artistInfoBioContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setUrl(artistInfoUrl: String) {
        openUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, artistInfoUrl) }
    }

}