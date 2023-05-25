package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.LAST_FM_DEFAULT_IMAGE
import ayds.lisboa3.submodule.lastFm.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.LastFmService

interface ProxyLastFm {
    fun getCard(artistName: String):Card
}

internal class ProxyLastFmImpl(private val lastFmService: LastFmService) : ProxyLastFm {
    override fun getCard(artistName: String): Card {
        val lastFmArtistInfo = lastFmService.getArtistInfo(artistName)
        return adaptLastFmArtistInfoToCard(lastFmArtistInfo) ?: Card(source = Source.LastFm, sourceLogo = LAST_FM_DEFAULT_IMAGE)
    }

    private fun adaptLastFmArtistInfoToCard(lastFmArtistInfo: LastFmArtistInfo?) =
        lastFmArtistInfo?.let {
            Card(description = it.bioContent,
                infoUrl = it.url,
                source = Source.LastFm,
                sourceLogo = LAST_FM_DEFAULT_IMAGE)
        }
}
