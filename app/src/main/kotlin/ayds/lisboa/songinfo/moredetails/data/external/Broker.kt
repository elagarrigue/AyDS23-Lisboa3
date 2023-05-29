package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface Broker{
    fun getCards(artistName: String):List<Card>
}
internal class BrokerImpl( private val proxyList: MutableList<ProxyInterface>):Broker {

    override fun getCards(artistName: String): List<Card> {
        val cards: MutableList<Card> = mutableListOf()

        proxyList.forEach{
            cards.add(it.getCard(artistName))
        }

        return cards
    }
}