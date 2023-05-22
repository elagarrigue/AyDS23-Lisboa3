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
            getArtistCards(artistName)
        }.start()
    }

    private fun getArtistCards(artistName: String) {
        val artistCards = artistInfoRepository.getArtistInfo(artistName)
        val uiState = getUiState(artistName, artistCards.first())
        uiEventObservable.notify(uiState)
    }

    private fun getUiState(artistName: String, artistCard: Card): OtherInfoUiState {
        return OtherInfoUiState(
            artistCardDescription = artistCardHelper.getArtistCardDescription(artistName, artistCard),
            artistCardInfoUrl = artistCardHelper.getArtistCardInfoUrl(artistCard),
            artistCardSourceLogo =  artistCardHelper.getArtistCardSourceLogo(artistCard)
        )
    }

}