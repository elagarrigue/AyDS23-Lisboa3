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

private const val NO_RESULTS = "NO RESULTS"

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

    private lateinit var noResultsTextView: TextView

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

        noResultsTextView = findViewById(R.id.noResults)
    }

    private fun subscribeToPresenter() {
        presenter = MoreDetailsInjector.getPresenter()
        presenter.uiEventObservable.subscribe(observer)
    }

    private fun disableCards() {
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
            showCards(uiState)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private data class CardViews(
        val layout: ConstraintLayout,
        val descriptionTextView: TextView,
        val imageView: ImageView,
        val openUrlButton: View,
        val sourceTextView: TextView
    )

    private fun showCards(uiState: OtherInfoUiState) {
        val artistCards = uiState.artistCards

        val cardViews = listOf(
            CardViews(layoutCard1, otherInfoTextViewCard1, imageViewCard1, openUrlButtonCard1, sourceTextViewCard1),
            CardViews(layoutCard2, otherInfoTextViewCard2, imageViewCard2, openUrlButtonCard2, sourceTextViewCard2),
            CardViews(layoutCard3, otherInfoTextViewCard3, imageViewCard3, openUrlButtonCard3, sourceTextViewCard3)
        )

        if (artistCards.isNotEmpty()) {
            cardViews.forEachIndexed { index, cardView ->
                val card = artistCards.getOrNull(index)

                if (card != null) {
                    cardView.descriptionTextView.text = HtmlCompat.fromHtml(card.formattedDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    imageLoader.loadImageIntoView(card.sourceLogo, cardView.imageView)
                    cardView.openUrlButton.setOnClickListener { navigationUtils.openExternalUrl(this, card.infoUrl) }
                    cardView.sourceTextView.text = card.title
                    cardView.layout.visibility = View.VISIBLE
                } else {
                    cardView.layout.visibility = View.GONE
                }
            }
        } else {
            showNoResults()
        }
    }

    private fun showNoResults() {
        noResultsTextView.text = NO_RESULTS
    }
}
