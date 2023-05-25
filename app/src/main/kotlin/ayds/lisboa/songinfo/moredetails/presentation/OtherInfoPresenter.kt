package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherInfoPresenter {
    val uiEventObservable: Observable<List<OtherInfoUiState>>

    fun fetch(artistName: String)
}

internal class OtherInfoPresenterImpl(private val artistInfoRepository: ArtistInfoRepository,
                                      private val artistCardHelper: ArtistCardHelper
): OtherInfoPresenter {

    private val onActionSubject = Subject<List<OtherInfoUiState>>()
    override val uiEventObservable = onActionSubject

    override fun fetch(artistName: String){
        Thread {
            getArtistCards(artistName)
        }.start()
    }

    private fun getArtistCards(artistName: String) {
        val artistCards = artistInfoRepository.getArtistInfo(artistName)
        val uiStates = getUiStates(artistName, artistCards)
        uiEventObservable.notify(uiStates)
    }

    private fun getUiStates(artistName: String, artistCards: List<Card>): List<OtherInfoUiState> {
        val uiStates: MutableList<OtherInfoUiState> = mutableListOf()
        artistCards.forEach{
            uiStates.add(OtherInfoUiState(
                artistCardDescription = artistCardHelper.getArtistCardDescription(artistName,it),
                artistCardInfoUrl = artistCardHelper.getArtistCardInfoUrl(it),
                artistCardSourceLogo =  artistCardHelper.getArtistCardSourceLogo(it)
            ))
        }
        return uiStates
    }

}