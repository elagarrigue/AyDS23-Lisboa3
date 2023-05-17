package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class OtherInfoPresenterTest {
    private val artistInfoRepository: ArtistInfoRepository = mockk()
    private val artistInfoHelper: ArtistInfoHelper= mockk()
    private val otherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository,artistInfoHelper)

    @Test
    fun `on fetch should notify view with uiState`() {
        val artistInfo:ArtistInfo = mockk()
        val bio = ""
        val url = ""
        every { artistInfoRepository.getArtistInfo("artist") } returns artistInfo
        every { artistInfoHelper.getArtistInfoText("artist",artistInfo) } returns bio
        every { artistInfoHelper.getArtistInfoUrl(artistInfo) } returns url
        val otherInfoUiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiEventObservable.subscribe {
            otherInfoUiStateTester(it)
        }
        val otherInfoUiState = OtherInfoUiState(
            artistInfoBioContent = bio,
            artistInfoUrl = url
        )
        otherInfoPresenter.fetch("artist")

        verify { otherInfoUiStateTester(otherInfoUiState) }

    }

}