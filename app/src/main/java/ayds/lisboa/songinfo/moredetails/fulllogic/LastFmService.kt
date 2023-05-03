package ayds.lisboa.songinfo.moredetails.fulllogic

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ayds.lisboa.songinfo.moredetails.fulllogic.ArtistInfo.LastFmArtistInfo


private const val LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

interface LastFmService {
    fun getArtistInfo(artistName: String): LastFmArtistInfo?

}
internal class LastFmServiceImpl(
    private val lastFMAPI: LastFmApi,
    private val retrofit: Retrofit,
    private val lastFmToArtistInfoResolver: LastFmToArtistInfoResolver): LastFmService {

    // INITS PASAR A INYECTOR
    private fun init() {
        initRetrofit()
        initLastFmApi()
    }

    private fun initRetrofit() {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.baseUrl(LAST_FM_API_BASE_URL)
        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
        retrofit = retrofitBuilder.build()
    }

    private fun initLastFmApi() {
        lastFMAPI = retrofit.create(LastFmApi::class.java)
    }

    override fun getArtistInfo(artistName: String): LastFmArtistInfo? {
        val serviceData = getArtistInfoFromService(artistName)
        return lastFmToArtistInfoResolver.getArtistInfoFromExternalData(serviceData.body())
    }

    private fun getArtistInfoFromService(artistName: String): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }


}