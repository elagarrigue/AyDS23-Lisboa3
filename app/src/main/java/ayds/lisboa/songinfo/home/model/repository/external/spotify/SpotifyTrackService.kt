package ayds.lisboa.songinfo.home.model.repository.external.spotify

import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

interface SpotifyTrackService {

    fun getSong(title: String): SpotifySong?
}