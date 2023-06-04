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
import java.lang.Exception

internal class CardsRepositoryTest {

    private val cardsLocalStorage: CardsLocalStorage = mockk(relaxUnitFun = true)
    private val cardsBroker : CardsBroker = mockk(relaxUnitFun = true)

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
        val artistInfo = listOf(
            ArtistCard("bioContent", "url", Source.LastFm),
            ArtistCard().
        every { cardsLocalStorage.getArtistCards("artistName") } returns listOf(artistInfo)

        val result = cardsRepository.getArtistCards("artistName")

        assertEquals(artistInfo, result)
        assertTrue(artistInfo.isLocallyStored)
    }

    @Test
    fun `given existing artist from service should get the artistInfo and store it`(){
        val artistInfo = LastFmArtistInfo("bioContent", "url", false)
        every { cardsLocalStorage.getArtistInfo("artistName") } returns null
        every { cardsBroker.getArtistInfo("artistName") } returns artistInfo

        val result = cardsRepository.getArtistCards("artistName")

        assertEquals(artistInfo, result)
        assertFalse(artistInfo.isLocallyStored)
        verify { cardsLocalStorage.saveArtist("artistName", artistInfo) }
    }

    @Test
    fun `given service exception should return emptyArtistInfo`() {
        every { cardsLocalStorage.getArtistInfo("artistName") } returns null
        every { cardsBroker.getArtistInfo("artistName") } throws mockk<Exception>()

        val result = cardsRepository.getArtistCards("artistName")

        assertEquals(ArtistInfo.EmptyArtistInfo, result)
    }

}