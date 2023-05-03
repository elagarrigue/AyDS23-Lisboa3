package ayds.lisboa.songinfo.moredetails.fulllogic

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LAST_FM_API_BASE_URL = "https://ws.audioscrobbler.com/2.0/"

interface LastFMService {
    fun init()
    fun getResponse(artistName: String): Response<String>
}
internal class LastFMServiceImpl: LastFMService {
    private lateinit var lastFMAPI: LastFMApi
    private lateinit var retrofit: Retrofit

    override fun init() {
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
        lastFMAPI = retrofit.create(LastFMApi::class.java)
    }

    override fun getResponse(artistName: String): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }


}