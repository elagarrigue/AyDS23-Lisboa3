package ayds.lisboa.songinfo.moredetails.fulllogic.view

import ayds.lisboa.songinfo.home.view.HomeUiEvent
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfoRepository
import ayds.observer.Observer

interface OtherInfoPresenter {
    fun setOtherInfoView(otherInfoView: OtherInfoView)
}

internal class OtherInfoPresenterImpl(private val artistInfoRepository: ArtistInfoRepository): OtherInfoPresenter {
    private lateinit var otherInfoView: OtherInfoView
    override fun setOtherInfoView(otherInfoView: OtherInfoView) {
        this.otherInfoView=otherInfoView
        otherInfoView.uiEventObservable.subscribe(observer)
    }

    private val observer: Observer<HomeUiEvent> =
        Observer { value -> HomeUiEvent.OpenSongUrl -> openSongUrl()
            }


    private fun openInfoUrl(){
        //TODO
    }

    private fun open() {
        getArtistInfoOnUpdateView()
    }

    private fun getArtistInfoOnUpdateView() {
        // MOVER METODO COMPLETO AL PRESENTER -> notify() por observer
        val artistName = otherInfoView.getArtistName()
        val artistInfo = artistInfoRepository.getArtistInfo(artistName)
        otherInfoView.updateView(artistName, artistInfo)
    }
}