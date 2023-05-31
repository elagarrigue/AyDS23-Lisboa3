package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard

interface ArtistInfoRepository {
    fun getArtistInfo(artistName:String): List<ArtistCard>
}