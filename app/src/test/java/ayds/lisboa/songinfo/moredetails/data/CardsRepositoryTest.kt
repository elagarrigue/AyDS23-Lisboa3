package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa3.submodule.lastFm.LastFmService
import ayds.lisboa.songinfo.moredetails.data.local.LastFmLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.Cards
import ayds.lisboa.songinfo.moredetails.domain.entities.Cards.Card
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.Exception

internal class CardsRepositoryTest {

    private val lastFmLocalStorage: LastFmLocalStorage = mockk(relaxUnitFun = true)
    private val lastFmService: LastFmService = mockk(relaxUnitFun = true)

    private val artistInfoRepository: ArtistInfoRepository by lazy {
        ArtistInfoRepositoryImpl(lastFmLocalStorage, lastFmService)
    }

    @Test
    fun `given non existing artist should return emptyArtistInfo`(){
        every { lastFmLocalStorage.getArtistCard("artistName") } returns null
        every { lastFmService.getArtistInfo("artistName") } returns null

        val result = artistInfoRepository.getArtistInfo("artistName")

        assertEquals(result, Cards.EmptyCards)
    }

    @Test
    fun `given existing artist from db should return artistInfo`(){
        val artistInfo = Card("bioContent", "url")
        every { lastFmLocalStorage.getArtistCard("artistName") } returns artistInfo

        val result = artistInfoRepository.getArtistInfo("artistName")

        assertEquals(artistInfo, result)
        assertTrue(artistInfo.isLocallyStored)
    }

    @Test
    fun `given existing artist from service should get the artistInfo and store it`(){
        val artistInfo = Card("bioContent", "url", false)
        every { lastFmLocalStorage.getArtistCard("artistName") } returns null
        every { lastFmService.getArtistInfo("artistName") } returns artistInfo

        val result = artistInfoRepository.getArtistInfo("artistName")

        assertEquals(artistInfo, result)
        assertFalse(artistInfo.isLocallyStored)
        verify { lastFmLocalStorage.saveArtistCard("artistName", artistInfo) }
    }

    @Test
    fun `given service exception should return emptyArtistInfo`() {
        every { lastFmLocalStorage.getArtistCard("artistName") } returns null
        every { lastFmService.getArtistInfo("artistName") } throws mockk<Exception>()

        val result = artistInfoRepository.getArtistInfo("artistName")

        assertEquals(Cards.EmptyCards, result)
    }

}