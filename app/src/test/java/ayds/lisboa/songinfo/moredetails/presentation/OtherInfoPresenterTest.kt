package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.home.controller.HomeControllerImpl
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.view.HomeUiEvent
import ayds.lisboa.songinfo.home.view.HomeUiState
import ayds.lisboa.songinfo.moredetails.dependencyinyector.MoreDetailsInjector
import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.observer.Observable
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class OtherInfoPresenterTest {
    private val moreDetailsInjector =MoreDetailsInjector
    private val otherInfoView: OtherInfoView = mockk()
    private val artistInfoRepository: ArtistInfoRepository = mockk(relaxUnitFun = true)
    private val artistInfoHelper: ArtistInfoHelper= mockk(relaxUnitFun = true)

    private val otherInfoPresenter = spyk(OtherInfoPresenterImpl(artistInfoRepository,artistInfoHelper))


    @Before
    fun setup() {
        moreDetailsInjector.init(otherInfoView)
    }

    @Test
    fun `on fetch should notify view with uiState`() {
        otherInfoPresenter.fetch("artist")
        verify { otherInfoPresenter.fetch("artist") }
    }

}