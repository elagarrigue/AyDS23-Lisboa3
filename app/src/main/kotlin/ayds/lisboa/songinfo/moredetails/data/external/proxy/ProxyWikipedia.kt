package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.winchester.artistinfo.external.DEFAULT_IMAGE
import ayds.winchester.artistinfo.external.WikipediaArtistInfo
import ayds.winchester.artistinfo.external.WikipediaService

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
            if (it.artistInfo.isNotEmpty()) {
                Card.ArtistCard(
                    description = it.artistInfo,
                    infoUrl = it.wikipediaUrl,
                    source = Source.Wikipedia,
                    sourceLogo = DEFAULT_IMAGE
                )
            } else {
                null
            }
        }
    }

}