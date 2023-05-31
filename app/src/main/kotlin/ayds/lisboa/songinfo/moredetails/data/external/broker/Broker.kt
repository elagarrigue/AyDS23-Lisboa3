package ayds.lisboa.songinfo.moredetails.data.external.broker

import ayds.lisboa.songinfo.moredetails.data.external.proxy.ProxyInterface
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard

interface Broker{
    fun getCards(artistName: String):List<ArtistCard>
}

internal class BrokerImpl(private val proxyList: List<ProxyInterface>): Broker {

    override fun getCards(artistName: String): List<ArtistCard> {
        val cards: MutableList<Card> = mutableListOf()

        proxyList.forEach{
            cards.add(it.getCard(artistName))
        }

        return cards.filterIsInstance<ArtistCard>()
    }
}