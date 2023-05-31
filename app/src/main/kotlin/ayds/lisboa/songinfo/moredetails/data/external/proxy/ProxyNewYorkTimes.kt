package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import com.test.artist.external.NYTimesArtistService
import com.test.artist.external.entities.Artist

const val NEW_YORK_TIMES_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/NewYorkTimes.svg/640px-NewYorkTimes.svg.png"

internal class ProxyNewYorkTimes(private val newYorkTimesService: NYTimesArtistService):
    ProxyInterface {
    override fun getCard(artistName: String): Card {
        val newYorkTimes = newYorkTimesService.getArtist(artistName)

        val result: Card = try {
            adaptNewYorkTimesToCard(newYorkTimes) ?: Card.EmptyCard
        } catch (ioException: Exception) {
            Card.EmptyCard
        }

        return result
    }

    private fun adaptNewYorkTimesToCard(nyTimesArtistInfo: Artist.NYTimesArtist?): Card.ArtistCard? {
        return nyTimesArtistInfo?.let {
            Card.ArtistCard(
                description = it.info.toString(),
                infoUrl = it.url.toString(),
                source = Source.NewYorkTimes,
                sourceLogo = NEW_YORK_TIMES_DEFAULT_IMAGE
            )
        }
    }
}