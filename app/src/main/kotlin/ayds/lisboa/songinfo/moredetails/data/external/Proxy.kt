package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.EmptyCard
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.LAST_FM_DEFAULT_IMAGE
import ayds.lisboa3.submodule.lastFm.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.LastFmService
import ayds.winchester.artistinfo.external.WikipediaArtistInfo
import ayds.winchester.artistinfo.external.WikipediaService
import com.test.artist.external.NYTimesArtistService
import com.test.artist.external.entities.Artist

interface ProxyInterface {
    fun getCard(artistName: String): Card
}

internal class ProxyLastFm(private val lastFmService: LastFmService) : ProxyInterface {
    override fun getCard(artistName: String): Card {
        val lastFmArtistInfo = lastFmService.getArtistInfo(artistName)
        return adaptLastFmArtistInfoToCard(lastFmArtistInfo) ?: EmptyCard
    }

    private fun adaptLastFmArtistInfoToCard(lastFmArtistInfo: LastFmArtistInfo?): Card.ArtistCard? {
        return lastFmArtistInfo?.let {
            ArtistCard(
                description = it.bioContent,
                infoUrl = it.url,
                source = Source.LastFm.position,
                sourceLogo = LAST_FM_DEFAULT_IMAGE
            )
        }
    }
}

const val NEW_YORK_TIMES_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/NewYorkTimes.svg/640px-NewYorkTimes.svg.png"

internal class ProxyNewYorkTimes(private val newYorkTimesService: NYTimesArtistService): ProxyInterface{
    override fun getCard(artistName: String): Card {
        val newYorkTimes = newYorkTimesService.getArtist(artistName)
        return adaptNewYorkTimesToCard(newYorkTimes) ?: EmptyCard
    }

    private fun adaptNewYorkTimesToCard(nyTimesArtistInfo: Artist.NYTimesArtist?): ArtistCard? {
        return nyTimesArtistInfo?.let {
            ArtistCard(
                description = it.info.toString(),
                infoUrl = it.url.toString(),
                source = Source.NewYorkTimes.position,
                sourceLogo = NEW_YORK_TIMES_DEFAULT_IMAGE
            )
        }
    }
}

const val WIKIPEDIA_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Wikipedia-logo-v2-wordmark.svg/1024px-Wikipedia-logo-v2-wordmark.svg.png?20180129141506"

internal class ProxyWikipedia(private val wikipediaService: WikipediaService) : ProxyInterface{
    override fun getCard(artistName: String): Card {
        val wikipedia = wikipediaService.getArtist(artistName)
        return adaptWikipediaToCard(wikipedia) ?: EmptyCard
    }


    private fun adaptWikipediaToCard(wikipedia: WikipediaArtistInfo?): ArtistCard? {
        return wikipedia?.let {
            ArtistCard(
                description = it.artistInfo,
                infoUrl = it.wikipediaUrl,
                source = Source.Wikipedia.position,
                sourceLogo = WIKIPEDIA_DEFAULT_IMAGE
            )
        }
    }

}