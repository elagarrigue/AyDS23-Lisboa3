package ayds.lisboa.songinfo.moredetails.data.external.broker.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.LAST_FM_DEFAULT_IMAGE
import ayds.lisboa3.submodule.lastFm.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.LastFmService

internal class CardProxyLastFm(private val lastFmService: LastFmService) : CardProxy {
    override fun getCard(artistName: String): Card {
        val lastFmArtistInfo = lastFmService.getArtistInfo(artistName)

        val result: Card = try {
            adaptLastFmArtistInfoToCard(lastFmArtistInfo) ?: Card.EmptyCard
        } catch (ioException: Exception) {
            Card.EmptyCard
        }

        return result
    }

    private fun adaptLastFmArtistInfoToCard(lastFmArtistInfo: LastFmArtistInfo?): Card.ArtistCard? {
        return lastFmArtistInfo?.let {
            if (it.bioContent.isNotEmpty()) {
                Card.ArtistCard(
                    description = it.bioContent,
                    infoUrl = it.url,
                    source = Source.LastFm,
                    sourceLogo = LAST_FM_DEFAULT_IMAGE
                )
            } else {
                null
            }
        }
    }
}