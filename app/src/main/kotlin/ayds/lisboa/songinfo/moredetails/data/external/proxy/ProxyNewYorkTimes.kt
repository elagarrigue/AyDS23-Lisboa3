package ayds.lisboa.songinfo.moredetails.data.external.proxy

import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import ayds.lisboa3.submodule.lastFm.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.LastFmService

const val NEW_YORK_TIMES_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface ProxyNewYorkTimes{
    fun getCard(artistName: String):Card
}
internal class ProxyNewYorkTimesImpl(private val newYorkTimesService: LastFmService): ProxyNewYorkTimes{
    override fun getCard(artistName: String): Card {
        val newYorkTimes = newYorkTimesService.getArtistInfo(artistName)
        return adaptNewYorkTimesToCard(newYorkTimes) ?: Card(source = Source.NewYorkTimes, sourceLogo = NEW_YORK_TIMES_DEFAULT_IMAGE)
    }

    private fun adaptNewYorkTimesToCard(lastFmArtistInfo: LastFmArtistInfo?) =
        lastFmArtistInfo?.let {
            Card(description = lastFmArtistInfo.bioContent,
                infoUrl = lastFmArtistInfo.url,
                source = Source.NewYorkTimes,
                sourceLogo = NEW_YORK_TIMES_DEFAULT_IMAGE)
        }
}