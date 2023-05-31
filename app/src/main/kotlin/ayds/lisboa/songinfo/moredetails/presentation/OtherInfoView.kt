package ayds.lisboa.songinfo.moredetails.presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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

    private lateinit var layoutCard1: ConstraintLayout
    private lateinit var otherInfoTextViewCard1: TextView
    private lateinit var imageViewCard1: ImageView
    private lateinit var sourceTextViewCard1: TextView
    private lateinit var openUrlButtonCard1: View

    private lateinit var layoutCard2: ConstraintLayout
    private lateinit var otherInfoTextViewCard2: TextView
    private lateinit var imageViewCard2: ImageView
    private lateinit var sourceTextViewCard2: TextView
    private lateinit var openUrlButtonCard2: View

    private lateinit var layoutCard3: ConstraintLayout
    private lateinit var otherInfoTextViewCard3: TextView
    private lateinit var imageViewCard3: ImageView
    private lateinit var sourceTextViewCard3: TextView
    private lateinit var openUrlButtonCard3: View

    private lateinit var loadingProgressBar: ProgressBar

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
        disableCards()
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
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        layoutCard1 = findViewById(R.id.layoutCard1)
        otherInfoTextViewCard1 = findViewById(R.id.otherInfoTextViewCard1)
        imageViewCard1 = findViewById(R.id.imageViewCard1)
        sourceTextViewCard1 = findViewById(R.id.sourceTextViewCard1)
        openUrlButtonCard1 = findViewById(R.id.openUrlButtonCard1)

        layoutCard2 = findViewById(R.id.layoutCard2)
        otherInfoTextViewCard2 = findViewById(R.id.otherInfoTextViewCard2)
        imageViewCard2 = findViewById(R.id.imageViewCard2)
        sourceTextViewCard2 = findViewById(R.id.sourceTextViewCard2)
        openUrlButtonCard2 = findViewById(R.id.openUrlButtonCard2)

        layoutCard3 = findViewById(R.id.layoutCard3)
        otherInfoTextViewCard3 = findViewById(R.id.otherInfoTextViewCard3)
        imageViewCard3 = findViewById(R.id.imageViewCard3)
        sourceTextViewCard3 = findViewById(R.id.sourceTextViewCard3)
        openUrlButtonCard3 = findViewById(R.id.openUrlButtonCard3)
    }

    private fun subscribeToPresenter() {
        presenter = MoreDetailsInjector.getPresenter()
        presenter.uiEventObservable.subscribe(observer)
    }

    private fun disableCards() {
        /*
        layoutCard1.visibility = if (isVisible) View.VISIBLE else View.GONE
        layoutCard2.visibility = if (isVisible) View.VISIBLE else View.GONE
        layoutCard3.visibility = if (isVisible) View.VISIBLE else View.GONE
         */

        layoutCard1.visibility = View.GONE
        layoutCard2.visibility = View.GONE
        layoutCard3.visibility = View.GONE
    }

    private fun open() {
        presenter.fetch(getArtistName())
    }

    private val observer: Observer<OtherInfoUiState> =
        Observer { value -> updateView(value) }

    private fun updateView(uiState: OtherInfoUiState) {
        runOnUiThread {
            showLoading(true)

            setCard1(uiState.artistCards[0])
            setCard2(uiState.artistCards[1])
            setCard3(uiState.artistCards[2])

            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showCards(uiState: OtherInfoUiState) {
        val artistCards = uiState.artistCards

        when(artistCards.size) {
            1 -> {setCard1(artistCards[0])}
            2 -> {setCard1(artistCards[0]); setCard2(artistCards[1])}
            3 -> {setCard1(artistCards[0]); setCard2(artistCards[1]); setCard3(artistCards[2])}
            else -> showNoResults()
        }
    }

    private fun setCard1(card: ArtistCardState) {
        otherInfoTextViewCard1.text = HtmlCompat.fromHtml(card.descriptionFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonCard1.setOnClickListener { navigationUtils.openExternalUrl(this, card.infoUrl) }
        sourceTextViewCard1.text = card.title
        imageLoader.loadImageIntoView(card.sourceLogo, imageViewCard1)
        layoutCard1.visibility = View.VISIBLE
    }

    private fun setCard2(card: ArtistCardState) {
        otherInfoTextViewCard2.text = HtmlCompat.fromHtml(card.descriptionFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonCard2.setOnClickListener { navigationUtils.openExternalUrl(this, card.infoUrl) }
        sourceTextViewCard2.text = card.title
        imageLoader.loadImageIntoView(card.sourceLogo, imageViewCard2)
        layoutCard2.visibility = View.VISIBLE
    }

    private fun setCard3(card: ArtistCardState) {
        otherInfoTextViewCard3.text = HtmlCompat.fromHtml(card.descriptionFormatted, HtmlCompat.FROM_HTML_MODE_LEGACY)
        openUrlButtonCard3.setOnClickListener { navigationUtils.openExternalUrl(this, card.infoUrl) }
        sourceTextViewCard3.text = card.title
        imageLoader.loadImageIntoView(card.sourceLogo, imageViewCard3)
        layoutCard3.visibility = View.VISIBLE
    }

    private fun showNoResults() {
        TODO("Not yet implemented")
    }
}
