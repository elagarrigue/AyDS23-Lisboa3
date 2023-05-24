package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.external.LastFmService

interface ProxyNewYorkTimes{
    fun getCard(artistName: String):Card
}
internal class ProxyNewYorkTimesImpl(private val lastFmService: LastFmService): ProxyNewYorkTimes{
    override fun getCard(artistName: String): Card {
        TODO("Not yet implemented")
    }

}