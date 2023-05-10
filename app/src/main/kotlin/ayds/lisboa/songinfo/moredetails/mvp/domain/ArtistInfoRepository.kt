package ayds.lisboa.songinfo.moredetails.mvp.domain

interface ArtistInfoRepository {
    fun getArtistInfo(artistName:String): ArtistInfo
}