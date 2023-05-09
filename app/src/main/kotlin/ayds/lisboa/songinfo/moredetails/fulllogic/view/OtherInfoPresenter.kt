package ayds.lisboa.songinfo.moredetails.fulllogic.view

import ayds.lisboa.songinfo.moredetails.fulllogic.domain.ArtistInfoRepository

interface OtherInfoPresenter {
    fun setOtherInfoView(otherInfoView: OtherInfoView)
}

internal class OtherInfoPresenterImpl(private val artistInfoRepository: ArtistInfoRepository): OtherInfoPresenter {
    private lateinit var otherInfoView: OtherInfoView

    override fun setOtherInfoView(otherInfoView: OtherInfoView) {
        this.otherInfoView = otherInfoView
        open()
    }

    private fun open(){
        Thread {
            getArtistInfoOnUpdateView()
        }.start()
    }

    private fun getArtistInfoOnUpdateView() {
        val artistName = otherInfoView.getArtistName()
        val artistInfo = artistInfoRepository.getArtistInfo(artistName)

        otherInfoView.updateView(artistInfo)
    }

}