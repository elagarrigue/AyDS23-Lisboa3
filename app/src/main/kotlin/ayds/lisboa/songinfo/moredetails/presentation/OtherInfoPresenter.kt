package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherInfoPresenter {
    val uiEventObservable: Observable<OtherInfoUiState>

    fun fetch(artistName: String)
}

internal class OtherInfoPresenterImpl(private val artistInfoRepository: ArtistInfoRepository,
                                      private val artistCardHelper: ArtistCardHelper
): OtherInfoPresenter {

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

    private fun getUiState(artistName: String, card: Card): OtherInfoUiState {
        return OtherInfoUiState(
            artistCardDescription = artistCardHelper.getArtistCardDescription(artistName, card),
            artistCardInfoUrl = artistCardHelper.getArtistCardInfoUrl(card),
            artistCardSourceLogo =  artistCardHelper.getArtistCardSourceLogo(card)
        )
    }

}