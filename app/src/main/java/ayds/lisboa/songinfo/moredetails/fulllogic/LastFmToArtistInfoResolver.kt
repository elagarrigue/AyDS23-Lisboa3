package ayds.lisboa.songinfo.moredetails.fulllogic

import com.google.gson.Gson
import com.google.gson.JsonObject

private const val JSON_ARTIST = "artist"
private const val JSON_BIO = "bio"
private const val JSON_CONTENT = "content"
private const val JSON_URL = "url"

interface LastFmToArtistInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo?
}
internal class LastFmToArtistInfoResolverImpl: LastFmToArtistInfoResolver {

    override fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo.LastFmArtistInfo? =
        try {
            serviceData?.getArtist()?.let { item ->
                ArtistInfo.LastFmArtistInfo(
                    item.getBioContent(),
                    item.getUrl()
                )
            }
        } catch (e: Exception) {
            null
        }


    private fun String?.getArtist(): JsonObject {
        val jsonServiceData = Gson().fromJson(this, JsonObject::class.java)
        val jsonArtist = jsonServiceData[JSON_ARTIST]

        return jsonArtist.asJsonObject
    }

    private fun JsonObject.getBioContent(): String {
        val bio = this[JSON_BIO].asJsonObject
        val bioContent = bio[JSON_CONTENT]

        return bioContent.asString
    }

    private fun JsonObject.getUrl(): String {
        val url = this[JSON_URL]

        return url.asString
    }
}