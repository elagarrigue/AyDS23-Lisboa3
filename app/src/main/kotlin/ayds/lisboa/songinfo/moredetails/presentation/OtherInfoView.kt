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

    private lateinit var lastFmTextView: TextView
    private lateinit var lastFmImageView: ImageView
    private lateinit var lastFmUrlButton: View

    private lateinit var wikipediaTextView: TextView
    private lateinit var wikipediaImageView: ImageView
    private lateinit var wikipediaUrlButton: View

    private lateinit var newYorkTimesTextView: TextView
    private lateinit var newYorkTimesImageView: ImageView
    private lateinit var newYorkTimesUrlButton: View

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
        lastFmTextView = findViewById(R.id.otherInfoTextView)
        lastFmImageView = findViewById(R.id.imageView)
        lastFmUrlButton = findViewById(R.id.openUrlButton)

        wikipediaTextView = findViewById(R.id.otherInfoTextView2)
        wikipediaImageView = findViewById(R.id.imageView2)
        wikipediaUrlButton = findViewById(R.id.openUrlButton2)

        newYorkTimesTextView = findViewById(R.id.otherInfoTextView3)
        newYorkTimesImageView = findViewById(R.id.imageView3)
        newYorkTimesUrlButton = findViewById(R.id.openUrlButton3)
    }

    private fun subscribeToPresenter() {
        presenter = MoreDetailsInjector.getPresenter()
        presenter.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<List<OtherInfoUiState>> =
        Observer { value -> updateView(value) }

    private fun open() {
        presenter.fetch(getArtistName())
    }

    private fun updateView(uiStates: List<OtherInfoUiState>) {
        runOnUiThread {
                setDescription(uiStates)
                setInfoUrl(uiStates)
                setSourceLogo(uiStates)
        }
    }

    private fun setDescription(uiStates: List<OtherInfoUiState>) {
        val lastFmCardDescription = uiStates[0].artistCardDescription
        val wikipediaCardDescription = uiStates[1].artistCardDescription
        val newYorkTimesCardDescription = uiStates[2].artistCardDescription
        lastFmTextView.text = HtmlCompat.fromHtml(lastFmCardDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
        wikipediaTextView.text = HtmlCompat.fromHtml(wikipediaCardDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
        newYorkTimesTextView.text = HtmlCompat.fromHtml(newYorkTimesCardDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setInfoUrl(uiStates: List<OtherInfoUiState>) {
        val lastFmCardInfoUrl= uiStates[0].artistCardInfoUrl
        val wikipediaCardInfoUrl = uiStates[1].artistCardInfoUrl
        val newYorkTimesCardInfoUrl = uiStates[2].artistCardInfoUrl
        lastFmUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, lastFmCardInfoUrl) }
        wikipediaUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, wikipediaCardInfoUrl) }
        newYorkTimesUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, newYorkTimesCardInfoUrl) }
    }

    private fun setSourceLogo(uiStates: List<OtherInfoUiState>) {
        val lastFmCardSourceLogo = uiStates[0].artistCardSourceLogo
        val wikipediaCardSourceLogo = uiStates[1].artistCardSourceLogo
        val newYorkTimesCardSourceLogo = uiStates[2].artistCardSourceLogo
        imageLoader.loadImageIntoView(lastFmCardSourceLogo, lastFmImageView)
        imageLoader.loadImageIntoView(wikipediaCardSourceLogo, wikipediaImageView)
        imageLoader.loadImageIntoView(newYorkTimesCardSourceLogo, newYorkTimesImageView)
    }



}