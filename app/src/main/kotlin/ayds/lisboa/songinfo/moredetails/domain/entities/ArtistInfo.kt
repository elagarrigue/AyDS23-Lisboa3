package ayds.lisboa.songinfo.moredetails.domain.entities

sealed class ArtistInfo {
    data class LastFmArtistInfo (
        var bioContent : String,
        var url : String,
        var isLocallyStored : Boolean = false
        ) : ArtistInfo()
    object EmptyArtistInfo : ArtistInfo()
}