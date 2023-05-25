package ayds.lisboa.songinfo.moredetails.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.dependencyinyector.MoreDetailsInjector
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.navigation.NavigationUtils
import ayds.lisboa.songinfo.utils.view.ImageLoader
import ayds.observer.Observer

class OtherInfoView: AppCompatActivity() {
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils
    private val imageLoader: ImageLoader = UtilsInjector.imageLoader

    private lateinit var textViewLastFm: TextView
    private lateinit var imageViewLastFm: ImageView
    private lateinit var openUrlButtonLastFm: View

    private lateinit var textViewNewYorTimes: TextView
    private lateinit var imageViewNewYorkTimes: ImageView
    private lateinit var openUrlButtonNewYorkTimes: View

    private lateinit var textViewWikipedia: TextView
    private lateinit var imageViewWikipedia: ImageView
    private lateinit var openUrlButtonWikipedia: View

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
        textViewLastFm = findViewById(R.id.otherInfoTextViewLastFm)
        imageViewLastFm = findViewById(R.id.imageViewLastFm)
        openUrlButtonLastFm = findViewById(R.id.openUrlButtonLastFm)

        textViewNewYorTimes = findViewById(R.id.otherInfoTextViewNewYorkTimes)
        imageViewNewYorkTimes = findViewById(R.id.imageViewNewYorkTimes)
        openUrlButtonNewYorkTimes = findViewById(R.id.openUrlButtonNewYorkTimes)

        textViewWikipedia = findViewById(R.id.otherInfoTextViewWikipedia)
        imageViewWikipedia = findViewById(R.id.imageViewWikipedia)
        openUrlButtonWikipedia = findViewById(R.id.openUrlButtonWikipedia)
    }

    private fun subscribeToPresenter() {
        presenter = MoreDetailsInjector.getPresenter()
        presenter.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<OtherInfoUiState> =
        Observer { value -> updateView(value) }

    private fun updateView(uiState: OtherInfoUiState) {
        runOnUiThread {
            setLastFmCard(uiState.lastFmCard)
            setNewYorkTimesCard(uiState.newYorkTimesCard)
            setWikipediaCard(uiState.wikipediaCard)
        }
    }

    private fun setLastFmCard(lastFmCard: Card) {
        textViewLastFm.text = HtmlCompat.fromHtml(lastFmCard.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonLastFm.setOnClickListener { navigationUtils.openExternalUrl(this, lastFmCard.infoUrl) }
        imageLoader.loadImageIntoView(lastFmCard.sourceLogo, imageViewLastFm)
    }

    private fun setNewYorkTimesCard(newYorkTimesCard: Card) {
        textViewNewYorTimes.text = HtmlCompat.fromHtml(newYorkTimesCard.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonNewYorkTimes.setOnClickListener { navigationUtils.openExternalUrl(this, newYorkTimesCard.infoUrl) }
        imageLoader.loadImageIntoView(newYorkTimesCard.sourceLogo, imageViewNewYorkTimes)
    }

    private fun setWikipediaCard(wikipediaCard: Card) {
        textViewWikipedia.text = HtmlCompat.fromHtml(wikipediaCard.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonWikipedia.setOnClickListener { navigationUtils.openExternalUrl(this, wikipediaCard.infoUrl) }
        imageLoader.loadImageIntoView(wikipediaCard.sourceLogo, imageViewWikipedia)
    }

    private fun open() {
        presenter.fetch(getArtistName())
    }
}