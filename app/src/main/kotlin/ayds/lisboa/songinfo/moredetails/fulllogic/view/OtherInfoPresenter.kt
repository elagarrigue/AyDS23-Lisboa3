package ayds.lisboa.songinfo.moredetails.fulllogic.view

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfo
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfoRepository
import ayds.observer.Observer

interface OtherInfoPresenter {
    fun setOtherInfoView(otherInfoView: OtherInfoView)
}

internal class OtherInfoPresenterImpl(private val artistInfoRepository: ArtistInfoRepository): OtherInfoPresenter {
    private lateinit var otherInfoView: OtherInfoView

    override fun setOtherInfoView(otherInfoView: OtherInfoView) {
        this.otherInfoView = otherInfoView
        otherInfoView.uiEventObservable.subscribe(observer)
        open()
    }

    private val observer: Observer<OtherInfoEvent> =
        Observer { value ->
            when (value) {
                OtherInfoEvent.OpenInfoUrl -> openInfoUrl()
            }
        }

    private fun openInfoUrl() {
       otherInfoView.openExternalLink(otherInfoView.uiState.artistInfoUrl)
    }

    private fun open(){
        Thread {
            getArtistInfoOnUpdateView()
        }.start()
    }

    private fun getArtistInfoOnUpdateView() {
        val artistName = otherInfoView.getArtistName()
        val artistInfo = artistInfoRepository.getArtistInfo(artistName)
        updateUiState(artistInfo)
        otherInfoView.updateView()
    }

    private fun updateUiState(artistInfo: ArtistInfo) {
        when (artistInfo) {
            is ArtistInfo.LastFmArtistInfo -> updateOtherInfoState(artistInfo)
            ArtistInfo.EmptyArtistInfo -> updateNoResultsUiState()
        }
    }

    private fun updateOtherInfoState(artistInfo: ArtistInfo.LastFmArtistInfo) {
        otherInfoView.uiState = otherInfoView.uiState.copy(
            artistInfoBioContent = artistInfo.bioContent,
            artistInfoUrl = artistInfo.url
        )
    }

    private fun updateNoResultsUiState() {
        otherInfoView.uiState = otherInfoView.uiState.copy(
            artistInfoBioContent = "NO RESULTS",
            artistInfoUrl = ""
        )
    }

}