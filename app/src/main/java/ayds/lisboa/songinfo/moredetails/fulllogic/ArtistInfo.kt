package ayds.lisboa.songinfo.moredetails.fulllogic

import ayds.lisboa.songinfo.home.model.entities.Song

sealed class ArtistInfo {
    data class  SpotifyArtistInfo (
        var bioContent : String,
        var url : String,
        var isLocallyStored : Boolean = false
        ) : ArtistInfo()
    object EmptyArtistInfo : Song()

}