package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

import ayds.lisboa.songinfo.moredetails.data.external.proxy.ProxyLastFm
import ayds.lisboa.songinfo.moredetails.data.external.proxy.ProxyNewYorkTimes
import ayds.lisboa.songinfo.moredetails.data.external.proxy.ProxyWikipedia

interface Broker{
    fun getCards(artistName: String):List<Card>
}
internal class BrokerImpl(private val proxyLastFm: ProxyLastFm,
                          private val proxyNewYorkTimes: ProxyNewYorkTimes,
                          private val proxyWikipedia: ProxyWikipedia)
:Broker {

    override fun getCards(artistName: String): List<Card> {
        val cards: List<Card> = emptyList()
        cards.toMutableList().add(proxyLastFm.getCard(artistName))
        //cards.toMutableList().add(proxyNewYorkTimes.getCard(artistName))
        //cards.toMutableList().add(proxyWikipedia.getCard(artistName))
        return cards
    }
}