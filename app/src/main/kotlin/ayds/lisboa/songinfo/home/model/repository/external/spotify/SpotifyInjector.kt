package ayds.lisboa.songinfo.home.model.repository.external.spotify

import ayds.lisboa.songinfo.home.model.repository.external.spotify.tracks.*

object SpotifyInjector {

    val spotifyTrackService: SpotifyTrackService = SpotifyTrackInjector.spotifyTrackService
}