package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard

interface CardsRepository {
    fun getArtistCards(artistName:String): List<ArtistCard>
}