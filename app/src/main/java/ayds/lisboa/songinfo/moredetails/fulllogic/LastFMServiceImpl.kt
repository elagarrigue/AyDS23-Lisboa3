package ayds.lisboa.songinfo.moredetails.fulllogic

import retrofit2.Response

class LastFMServiceImpl {
    private lateinit var lastFMAPI: LastFMAPI

    private fun getResponse(artistName: String): Response<String> {
        return lastFMAPI.getArtistInfo(artistName).execute()
    }
}