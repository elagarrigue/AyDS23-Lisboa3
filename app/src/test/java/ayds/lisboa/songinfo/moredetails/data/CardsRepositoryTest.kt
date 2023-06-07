package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.external.broker.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.local.CardsLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa.songinfo.moredetails.domain.repository.CardsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class CardsRepositoryTest {

    private val cardsLocalStorage: CardsLocalStorage = mockk(relaxUnitFun = true)
    private val cardsBroker: CardsBroker = mockk(relaxUnitFun = true)

    private val cardsRepository: CardsRepository by lazy {
        CardsRepositoryImpl(cardsLocalStorage, cardsBroker)
    }

    @Test
    fun `given non existing artist should return emptyArtistInfo`(){
        every { cardsLocalStorage.getArtistCards("artistName") } returns emptyList()
        every { cardsBroker.getArtistCards("artistName") } returns emptyList()

        val result = cardsRepository.getArtistCards("artistName")

        assertEquals(result, emptyList<ArtistCard>())
    }

    @Test
    fun `given existing artist from db should return artistInfo`(){
        val artistCards = listOf(
            ArtistCard("description", "infoUrl", Source.LastFm, "sourceLogo"),
            ArtistCard("description", "infoUrl", Source.NewYorkTimes, "sourceLogo"),
            ArtistCard("description", "infoUrl", Source.Wikipedia, "sourceLogo")
        )
        every { cardsLocalStorage.getArtistCards("artistName") } returns artistCards

        val result = cardsRepository.getArtistCards("artistName")

        assertEquals(artistCards, result)
        artistCards.forEach { assertTrue(it.isLocallyStored) }
    }

    @Test
    fun `given existing artist from service should get the artistInfo and store it`(){
        val artistCards = listOf(
            ArtistCard("description", "infoUrl", Source.LastFm, "sourceLogo", false),
            ArtistCard("description", "infoUrl", Source.NewYorkTimes, "sourceLogo", false),
            ArtistCard("description", "infoUrl", Source.Wikipedia, "sourceLogo", false)
        )
        every { cardsLocalStorage.getArtistCards("artistName") } returns emptyList()
        every { cardsBroker.getArtistCards("artistName") } returns artistCards

        val result = cardsRepository.getArtistCards("artistName")

        assertEquals(artistCards, result)
        artistCards.forEach {
            assertFalse(it.isLocallyStored)
            verify { cardsLocalStorage.saveArtistCard("artistName", it) }
        }
    }

}