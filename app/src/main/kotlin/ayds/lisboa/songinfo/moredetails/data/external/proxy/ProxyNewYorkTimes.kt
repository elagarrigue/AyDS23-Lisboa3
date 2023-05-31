package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.LAST_FM_DEFAULT_IMAGE
import ayds.newYork4.artist.external.NYTimesArtistService
import ayds.newYork4.artist.external.entities.Artist
import ayds.newYork4.artist.external.entities.NY_TIMES_LOGO_URL


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
            if (it.info.isNotEmpty()){
                Card.ArtistCard(
                    description = it.info,
                    infoUrl = it.url.toString(),
                    source = Source.NewYorkTimes,
                    sourceLogo = NY_TIMES_LOGO_URL
                )
            } else {
                null
            }
        }
    }
}