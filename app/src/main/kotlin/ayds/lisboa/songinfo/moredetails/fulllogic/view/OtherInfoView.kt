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
import ayds.lisboa.songinfo.moredetails.fulllogic.MoreDetailsInjector
import com.squareup.picasso.Picasso
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfo
import ayds.lisboa.songinfo.utils.UtilsInjector
import ayds.lisboa.songinfo.utils.navigation.NavigationUtils
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherInfoView{
    val uiEventObservable: Observable<OtherInfoEvent>
    var uiState: OtherInfoUiState

    fun getArtistName():String
    fun updateView()
    fun openExternalLink(url: String)
}
internal class OtherInfoViewImpl : AppCompatActivity(),OtherInfoView {
    private val onActionSubject = Subject<OtherInfoEvent>()
    private val navigationUtils: NavigationUtils = UtilsInjector.navigationUtils

    private lateinit var artistInfoTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var openUrlButton: View

    override val uiEventObservable = onActionSubject
    override var uiState: OtherInfoUiState = OtherInfoUiState()

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initModule()
        initContentView()
        initListeners()
        initView()
    }

    private fun initModule(){
        MoreDetailsInjector.init(this)
    }

    private fun initContentView() {
        setContentView(R.layout.activity_other_info)
    }

    private fun initView() {
        artistInfoTextView = findViewById(R.id.textPane2)
        imageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)

    }

    private fun initListeners(){
        openUrlButton.setOnClickListener {notifyInfoAction()}
    }

    override fun getArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    override fun updateView() {
        runOnUiThread {
            setDefaultImage()
            setBioContent()

        }
    }

    private fun setDefaultImage() {
        Picasso.get().load(uiState.lastFmDefaultImage).into(imageView)
    }

    private fun setBioContent() {
        artistInfoTextView.text = HtmlCompat.fromHtml(uiState.artistInfoBioContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun notifyInfoAction() {
        onActionSubject.notify(OtherInfoEvent.Open)
    }

    override fun openExternalLink(url: String) {
        navigationUtils.openExternalUrl(this, url)
    }

}