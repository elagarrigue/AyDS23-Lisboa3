package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.external.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.external.LastFmService

const val WIKIPEDIA_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface ProxyWikipedia {
    fun getCard(artistName: String):Card
}
internal class ProxyWikipediaImpl(private val wikipediaService: LastFmService) : ProxyWikipedia {
    override fun getCard(artistName: String): Card {
        val wikipedia = wikipediaService.getArtistInfo(artistName)
        return adaptWikipediaToCard(wikipedia) ?: Card(source = Source.Wikipedia)
    }

    private fun adaptWikipediaToCard(wikipedia: LastFmArtistInfo?) =
        wikipedia?.let {
            Card(wikipedia.bioContent, wikipedia.url, Source.Wikipedia, WIKIPEDIA_DEFAULT_IMAGE)
        }
}