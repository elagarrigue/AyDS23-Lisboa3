package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.domain.repository.CardsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class OtherInfoPresenterTest {

    private val cardsRepository = mockk<CardsRepository>()
    private val artistCardHelper = mockk<ArtistCardHelper>()
    private val otherInfoPresenter: OtherInfoPresenter by lazy {
        OtherInfoPresenterImpl(cardsRepository, artistCardHelper)
    }

    @Test
    fun `on fetch should notify subscribers with otherInfoUiState`() {
        val artistCards = listOf(
            ArtistCard("description", "infoUrl", Source.LastFm, "sourceLogo"),
            ArtistCard("description", "infoUrl", Source.NewYorkTimes, "sourceLogo"),
            ArtistCard("description", "infoUrl", Source.Wikipedia, "sourceLogo")
        )
        val artistCardsStates = listOf(
            ArtistCardState("formattedDescription", "infoUrl", "title", "sourceLogo"),
            ArtistCardState("formattedDescription", "infoUrl", "title", "sourceLogo"),
            ArtistCardState("formattedDescription", "infoUrl", "title", "sourceLogo")
        )
        val otherInfoUiStateTester: (OtherInfoUiState) -> Unit = mockk(relaxed = true)
        otherInfoPresenter.uiEventObservable.subscribe {
            otherInfoUiStateTester(it)
        }

        every { cardsRepository.getArtistCards("artist") } returns artistCards
        every { artistCardHelper.getArtistCards("artist", artistCards) } returns artistCardsStates
        val result = OtherInfoUiState(artistCardsStates)
        otherInfoPresenter.fetch("artist")

        verify { otherInfoUiStateTester(result) }
    }

}