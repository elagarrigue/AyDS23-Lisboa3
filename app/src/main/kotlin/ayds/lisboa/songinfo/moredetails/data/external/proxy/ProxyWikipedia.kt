package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.external.LastFmService

interface ProxyWikipedia{
    fun getCard(artistName: String):Card
}
internal class ProxyWikipediaImpl(private val lastFmService: LastFmService): ProxyWikipedia{
    override fun getCard(artistName: String): Card {
        TODO("Not yet implemented")
    }

}