package ayds.lisboa.songinfo.moredetails.mvp.presenter

import ayds.lisboa.songinfo.moredetails.mvp.domain.ArtistInfo
import ayds.lisboa.songinfo.moredetails.mvp.domain.ArtistInfoRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherInfoPresenter {
    val uiEventObservable: Observable<OtherInfoUiState>
    fun fetch(artistName: String)
}

internal class OtherInfoPresenterImpl(private val artistInfoRepository: ArtistInfoRepository,
                                      private val artistInfoHelper: ArtistInfoHelper): OtherInfoPresenter {

    private val onActionSubject = Subject<OtherInfoUiState>()
    override val uiEventObservable = onActionSubject

    override fun fetch(artistName: String){
        Thread {
            getArtistInfoOnUpdateView(artistName)
        }.start()
    }

    private fun getArtistInfoOnUpdateView(artistName: String) {
        val artistInfo = artistInfoRepository.getArtistInfo(artistName)
        val uiState = getUiState(artistName, artistInfo)
        uiEventObservable.notify(uiState)
    }

    private fun getUiState(artistName: String, artistInfo: ArtistInfo): OtherInfoUiState {
        return OtherInfoUiState(
            artistInfoBioContent = artistInfoHelper.getArtistInfoText(artistName, artistInfo),
            artistInfoUrl = artistInfoHelper.getArtistInfoUrl(artistInfo)
        )
    }

}