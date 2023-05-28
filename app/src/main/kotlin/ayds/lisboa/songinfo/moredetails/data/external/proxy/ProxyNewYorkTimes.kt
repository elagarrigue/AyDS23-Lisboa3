package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import com.test.artist.external.NYTimesArtistService
import com.test.artist.external.entities.Artist.NYTimesArtist

const val NEW_YORK_TIMES_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/NewYorkTimes.svg/640px-NewYorkTimes.svg.png"

interface ProxyNewYorkTimes{
    fun getCard(artistName: String):Card
}
internal class ProxyNewYorkTimesImpl(private val newYorkTimesService: NYTimesArtistService): ProxyNewYorkTimes{
    override fun getCard(artistName: String): Card {
        val newYorkTimes = newYorkTimesService.getArtist(artistName)
        return adaptNewYorkTimesToCard(newYorkTimes) ?: Card(source = Source.NewYorkTimes, sourceLogo = NEW_YORK_TIMES_DEFAULT_IMAGE)
    }

    private fun adaptNewYorkTimesToCard(nyTimesArtistInfo: NYTimesArtist?) =
        nyTimesArtistInfo?.let {
            Card(description = it.info.toString(),
                infoUrl = it.url.toString(),
                source = Source.NewYorkTimes,
                sourceLogo = NEW_YORK_TIMES_DEFAULT_IMAGE)
        }
}