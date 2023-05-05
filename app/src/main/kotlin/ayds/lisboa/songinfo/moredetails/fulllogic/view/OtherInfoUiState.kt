package ayds.lisboa.songinfo.moredetails.fulllogic.view

data class OtherInfoUiState(
    val artistInfoBioContent: String = "",
    val artistInfoUrl: String = "",
    val lastFmDefaultImage: String = LAST_FM_DEFAULT_IMAGE,
) {

    companion object {
        const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
}