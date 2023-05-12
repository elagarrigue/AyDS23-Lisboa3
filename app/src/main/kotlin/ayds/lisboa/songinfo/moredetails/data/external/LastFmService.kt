package ayds.lisboa.songinfo.moredetails.data.external

import retrofit2.Response
import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo.LastFmArtistInfo

interface LastFmService {
    fun getArtistInfo(artistName: String): LastFmArtistInfo?

}
internal class LastFmServiceImpl(
    private val lastFMAPI: LastFmApi,
    private val lastFmToArtistInfoResolver: LastFmToArtistInfoResolver
): LastFmService {

    override fun getArtistInfo(artistName: String): LastFmArtistInfo? {
        val serviceData = getArtistInfoFromService(artistName)
        return lastFmToArtistInfoResolver.getArtistInfoFromExternalData(serviceData.body())
    }

    private fun getArtistInfoFromService(artistName: String): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }

}