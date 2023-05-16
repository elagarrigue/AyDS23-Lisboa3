package ayds.lisboa.songinfo.moredetails.presentation


import ayds.lisboa.songinfo.moredetails.dependencyinyector.MoreDetailsInjector
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import ayds.observer.Subject
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class OtherInfoPresenterTest {
    private val moreDetailsInjector =MoreDetailsInjector
    private val otherInfoView: OtherInfoView = mockk()
    private val artistInfoRepository: ArtistInfoRepository = mockk(relaxed = true)
    private val artistInfoHelper: ArtistInfoHelper= mockk(relaxed = true)
    private val onActionSubject = Subject<OtherInfoUiState>()


    private val otherInfoPresenter = spyk(OtherInfoPresenterImpl(artistInfoRepository,artistInfoHelper))


    @Before
    fun setup() {
        moreDetailsInjector.init(otherInfoView)
    }

    @Test
    fun `on fetch should notify view with uiState`() {
        otherInfoPresenter.fetch("artist")

        verify { val artist =
            artistInfoRepository.getArtistInfo("artist")
            var otherInfoUiState = OtherInfoUiState(artistInfoHelper.getArtistInfoText("artist",artist),
                    artistInfoHelper.getArtistInfoUrl(artist))
           onActionSubject.notify(otherInfoUiState)
        }

    }

}