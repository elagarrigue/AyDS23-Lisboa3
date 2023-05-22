package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Cards
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class OtherInfoPresenterTest {
    private val artistInfoRepository: ArtistInfoRepository = mockk()
    private val artistCardHelper: ArtistCardHelper= mockk()
    private val otherInfoPresenter = OtherInfoPresenterImpl(artistInfoRepository,artistCardHelper)

    @Test
    fun `on fetch should notify subscribers with otherInfoUiState`() {
        val cards:Cards = mockk()
        val bio = ""
        val url = ""
        val otherInfoUiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiEventObservable.subscribe {
            otherInfoUiStateTester(it)
        }

        every { artistInfoRepository.getArtistInfo("artist") } returns cards
        every { artistCardHelper.getArtistCardDescription("artist",cards) } returns bio
        every { artistCardHelper.getArtistCardInfoUrl(cards) } returns url
        val otherInfoUiState = OtherInfoUiState(
            artistCardDescription = bio,
            artistCardInfoUrl = url
        )
        otherInfoPresenter.fetch("artist")

        verify { otherInfoUiStateTester(otherInfoUiState) }
    }

}