package ayds.lisboa.songinfo.moredetails.fulllogic

sealed class ArtistInfo {
    data class  SpotifyArtistInfo (
            val url : String,
            val bioContent : String,
            val isLocallyStored : Boolean
            ) : ArtistInfo()

    object EmptyArtist : ArtistInfo()
}