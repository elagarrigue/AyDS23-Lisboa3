package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.winchester.artistinfo.external.WikipediaArtistInfo
import ayds.winchester.artistinfo.external.WikipediaService

const val WIKIPEDIA_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Wikipedia-logo-v2-wordmark.svg/1024px-Wikipedia-logo-v2-wordmark.svg.png?20180129141506"

internal class ProxyWikipedia(private val wikipediaService: WikipediaService) : ProxyInterface {
    override fun getCard(artistName: String): Card {
        val wikipedia = wikipediaService.getArtist(artistName)

        val result: Card = try {
            adaptWikipediaToCard(wikipedia) ?: Card.EmptyCard
        } catch (ioException: Exception) {
            Card.EmptyCard
        }

        return result
    }

    private fun adaptWikipediaToCard(wikipedia: WikipediaArtistInfo?): Card.ArtistCard? {
        return wikipedia?.let {
            Card.ArtistCard(
                description = it.artistInfo,
                infoUrl = it.wikipediaUrl,
                source = Source.Wikipedia,
                sourceLogo = WIKIPEDIA_DEFAULT_IMAGE
            )
        }
    }

}