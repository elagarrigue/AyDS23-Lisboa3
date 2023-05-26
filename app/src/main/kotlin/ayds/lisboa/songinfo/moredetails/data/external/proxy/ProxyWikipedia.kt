package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.winchester.artistinfo.external.WikipediaArtistInfo
import ayds.winchester.artistinfo.external.WikipediaService

const val WIKIPEDIA_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Wikipedia-logo-v2-wordmark.svg/1024px-Wikipedia-logo-v2-wordmark.svg.png?20180129141506"
interface ProxyWikipedia {
    fun getCard(artistName: String):Card
}
internal class ProxyWikipediaImpl(private val wikipediaService: WikipediaService) : ProxyWikipedia {
    override fun getCard(artistName: String): Card {
        val wikipedia = wikipediaService.getArtist(artistName)
        return adaptWikipediaToCard(wikipedia) ?: Card(source = Source.Wikipedia, sourceLogo = WIKIPEDIA_DEFAULT_IMAGE)
    }

    private fun adaptWikipediaToCard(wikipedia: WikipediaArtistInfo?) =
        wikipedia?.let {
            Card(description = wikipedia.artistInfo,
                infoUrl = wikipedia.wikipediaUrl,
                source = Source.Wikipedia,
                sourceLogo = WIKIPEDIA_DEFAULT_IMAGE)
        }
}