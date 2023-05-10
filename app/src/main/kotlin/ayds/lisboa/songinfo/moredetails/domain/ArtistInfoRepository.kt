package ayds.lisboa.songinfo.moredetails.domain

interface ArtistInfoRepository {
    fun getArtistInfo(artistName:String): ArtistInfo
}