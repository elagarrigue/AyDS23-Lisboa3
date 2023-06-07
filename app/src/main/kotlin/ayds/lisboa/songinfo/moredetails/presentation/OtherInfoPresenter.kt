package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.repository.CardsRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface OtherInfoPresenter {
    val uiEventObservable: Observable<OtherInfoUiState>

    fun fetch(artistName: String)
}

internal class OtherInfoPresenterImpl(private val cardsRepository: CardsRepository, private val artistCardHelper: ArtistCardHelper): OtherInfoPresenter {

    private val onActionSubject = Subject<OtherInfoUiState>()
    override val uiEventObservable = onActionSubject

    override fun fetch(artistName: String){
        Thread {
            getArtistCards(artistName)
        }.start()
    }

    private fun getArtistCards(artistName: String) {
        val artistCards = cardsRepository.getArtistCards(artistName)
        val uiState = OtherInfoUiState(artistCardHelper.getArtistCards(artistName, artistCards))
        uiEventObservable.notify(uiState)
    }
}