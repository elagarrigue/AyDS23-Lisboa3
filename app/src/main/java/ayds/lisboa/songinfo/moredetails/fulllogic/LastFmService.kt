package ayds.lisboa.songinfo.moredetails.fulllogic

import retrofit2.Response

interface LastFmService {
    fun init()
    fun getResponse(artistName: String): Response<String>
}