package ayds.lisboa.songinfo.moredetails.mvp.domain

sealed class ArtistInfo {
    data class LastFmArtistInfo (
        var bioContent : String,
        var url : String,
        var isLocallyStored : Boolean = false
        ) : ArtistInfo()
    object EmptyArtistInfo : ArtistInfo()
}