package ayds.lisboa.songinfo.moredetails.domain.repository

import ayds.lisboa.songinfo.moredetails.domain.entities.ArtistInfo

interface ArtistInfoRepository {
    fun getArtistInfo(artistName:String): ArtistInfo
}