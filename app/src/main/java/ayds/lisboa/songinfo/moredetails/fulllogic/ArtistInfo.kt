package ayds.lisboa.songinfo.moredetails.fulllogic

sealed class ArtistInfo {
    data class  SpotifyArtistInfo (
        var bioContent : String,
        var url : String,
        var isLocallyStored : Boolean
        ) : ArtistInfo()
}