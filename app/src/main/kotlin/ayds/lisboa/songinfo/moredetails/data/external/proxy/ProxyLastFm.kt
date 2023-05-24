package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.LastFmService

const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface ProxyLastFm {
    fun getCard(artistName: String):Card
}
internal class ProxyLastFmImpl(private val lastFmService: LastFmService) : ProxyLastFm {
    override fun getCard(artistName: String): Card {
        val lastFmArtistInfo = lastFmService.getArtistInfo(artistName)
        return adaptLastFmArtistInfoToCard(lastFmArtistInfo) ?: Card(source = Source.LastFm)
    }

    private fun adaptLastFmArtistInfoToCard(lastFmArtistInfo: LastFmArtistInfo?) =
        lastFmArtistInfo?.let {
            Card(lastFmArtistInfo.bioContent, lastFmArtistInfo.url, Source.LastFm, LAST_FM_DEFAULT_IMAGE)
        }
}
