package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.data.LAST_FM_DEFAULT_IMAGE
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.external.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.external.LastFmService

interface ProxyLastFm{
    fun getCard(artistName: String):Card
}
internal class ProxyLastFmImpl(private val lastFmService: LastFmService):ProxyLastFm {

    override fun getCard(artistName: String): Card {
        val lastFmArtistInfo = lastFmService.getArtistInfo(artistName)
        return  adaptLastFmArtistInfoToCard(lastFmArtistInfo)
    }

    private fun adaptLastFmArtistInfoToCard(lastFmArtistInfo: LastFmArtistInfo?):Card{
        val card = Card( " ", "", Source.LastFm,"",false)
        if(lastFmArtistInfo != null){
            Card(lastFmArtistInfo.bioContent, lastFmArtistInfo.url, Source.LastFm, LAST_FM_DEFAULT_IMAGE)
        }
        return card
    }


}
