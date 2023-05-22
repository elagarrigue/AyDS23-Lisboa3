package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface ArtistInfoRepository {
    fun getArtistInfo(artistName:String): Card
}