package ayds.lisboa.songinfo.moredetails.data

import ayds.lisboa.songinfo.moredetails.data.external.LastFmService
import ayds.lisboa.songinfo.moredetails.data.local.LastFmLocalStorage
import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo
import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo.LastFmArtistInfo
import ayds.lisboa.songinfo.moredetails.domain.repository.ArtistInfoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.Exception

internal class ArtistInfoRepositoryTest {

    private val lastFmLocalStorage: LastFmLocalStorage = mockk(relaxUnitFun = true)
    private val lastFmService: LastFmService = mockk(relaxUnitFun = true)

    private val artistInfoRepository: ArtistInfoRepository by lazy {
        ArtistInfoRepositoryImpl(lastFmLocalStorage, lastFmService)
    }

    @Test
    fun `given non existing artist should return empty artist info`(){
        every { lastFmLocalStorage.getArtistInfo("artistName") } returns null
        every { lastFmService.getArtistInfo("artistName") } returns null

        val result = artistInfoRepository.getArtistInfo("artistName")

        assertEquals(result, ArtistInfo.EmptyArtistInfo)
    }

    @Test
    fun `given existing artist from db should return artist info`(){
        val artistInfo = LastFmArtistInfo("bioContent", "url")

        every { lastFmLocalStorage.getArtistInfo("artistName") } returns artistInfo

        val result = artistInfoRepository.getArtistInfo("artistName")

        assertEquals(artistInfo, result)
        assertTrue(artistInfo.isLocallyStored)
    }

    @Test
    fun `given existing artist from service should get the artist info and store it`(){
        val artistInfo = LastFmArtistInfo("bioContent", "url", false)

        every { lastFmLocalStorage.getArtistInfo("artistName") } returns null
        every { lastFmService.getArtistInfo("artistName") } returns artistInfo

        val result = artistInfoRepository.getArtistInfo("artistName")

        assertEquals(artistInfo, result)
        assertFalse(artistInfo.isLocallyStored)
        verify { lastFmLocalStorage.saveArtist("artistName", artistInfo) }
    }

    @Test
    fun `given service exception should return empty artist info`() {
        every { lastFmLocalStorage.getArtistInfo("artistName") } returns null
        every { lastFmService.getArtistInfo("artistName") } throws mockk<Exception>()

        val result = artistInfoRepository.getArtistInfo("artistName")

        assertEquals(ArtistInfo.EmptyArtistInfo, result)
    }

}