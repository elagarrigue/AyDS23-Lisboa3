package ayds.lisboa.songinfo.moredetails.fulllogic.domain

interface ArtistInfoRepository {
    fun getArtistInfo(artistName:String): ArtistInfo
}