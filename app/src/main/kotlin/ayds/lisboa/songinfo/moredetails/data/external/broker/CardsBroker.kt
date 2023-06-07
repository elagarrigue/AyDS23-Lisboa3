package ayds.lisboa.songinfo.moredetails.data.external.broker

import ayds.lisboa.songinfo.moredetails.data.external.broker.proxy.CardProxy
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard

interface CardsBroker{
    fun getArtistCards(artistName: String):List<ArtistCard>
}

internal class CardsBrokerImpl(private val cardProxyList: List<CardProxy>): CardsBroker {
    override fun getArtistCards(artistName: String): List<ArtistCard> {
        return cardProxyList.map { it.getCard(artistName) }.filterIsInstance<ArtistCard>()
    }
}