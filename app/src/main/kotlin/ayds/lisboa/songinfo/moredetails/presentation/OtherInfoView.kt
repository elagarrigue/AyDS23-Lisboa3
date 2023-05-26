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

    private lateinit var descriptionTextViewLastFm: TextView
    private lateinit var imageViewLastFm: ImageView
    private lateinit var sourceTextViewLastFm: TextView
    private lateinit var openUrlButtonLastFm: View

    private lateinit var descriptionTextViewNewYorkTimes: TextView
    private lateinit var imageViewNewYorkTimes: ImageView
    private lateinit var sourceTextViewNewYorkTimes: TextView
    private lateinit var openUrlButtonNewYorkTimes: View

    private lateinit var descriptionTextViewWikipedia: TextView
    private lateinit var imageViewWikipedia: ImageView
    private lateinit var sourceTextViewWikipedia: TextView
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
        descriptionTextViewLastFm = findViewById(R.id.otherInfoTextViewLastFm)
        imageViewLastFm = findViewById(R.id.imageViewLastFm)
        sourceTextViewLastFm = findViewById(R.id.sourceTextViewLastFm)
        openUrlButtonLastFm = findViewById(R.id.openUrlButtonLastFm)

        descriptionTextViewNewYorkTimes = findViewById(R.id.otherInfoTextViewNewYorkTimes)
        imageViewNewYorkTimes = findViewById(R.id.imageViewNewYorkTimes)
        sourceTextViewNewYorkTimes = findViewById(R.id.sourceTextViewNewYorkTimes)
        openUrlButtonNewYorkTimes = findViewById(R.id.openUrlButtonNewYorkTimes)

        descriptionTextViewWikipedia = findViewById(R.id.otherInfoTextViewWikipedia)
        imageViewWikipedia = findViewById(R.id.imageViewWikipedia)
        sourceTextViewWikipedia = findViewById(R.id.sourceTextViewWikipedia)
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
        descriptionTextViewLastFm.text = HtmlCompat.fromHtml(lastFmCard.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonLastFm.setOnClickListener { navigationUtils.openExternalUrl(this, lastFmCard.infoUrl) }
        sourceTextViewLastFm.text = lastFmCard.source.name
        imageLoader.loadImageIntoView(lastFmCard.sourceLogo, imageViewLastFm)
    }

    private fun setNewYorkTimesCard(newYorkTimesCard: Card) {
        descriptionTextViewNewYorkTimes.text = HtmlCompat.fromHtml(newYorkTimesCard.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonNewYorkTimes.setOnClickListener { navigationUtils.openExternalUrl(this, newYorkTimesCard.infoUrl) }
        sourceTextViewNewYorkTimes.text = newYorkTimesCard.source.name
        imageLoader.loadImageIntoView(newYorkTimesCard.sourceLogo, imageViewNewYorkTimes)
    }

    private fun setWikipediaCard(wikipediaCard: Card) {
        descriptionTextViewWikipedia.text = HtmlCompat.fromHtml(wikipediaCard.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonWikipedia.setOnClickListener { navigationUtils.openExternalUrl(this, wikipediaCard.infoUrl) }
        sourceTextViewWikipedia.text = wikipediaCard.source.name
        imageLoader.loadImageIntoView(wikipediaCard.sourceLogo, imageViewWikipedia)
    }

    private fun open() {
        presenter.fetch(getArtistName())
    }
}