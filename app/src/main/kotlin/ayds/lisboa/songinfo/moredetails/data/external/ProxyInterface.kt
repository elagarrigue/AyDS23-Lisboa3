package ayds.lisboa.songinfo.moredetails.data.external

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ProxyInterface {
    fun getCard(artistName: String): Card
}