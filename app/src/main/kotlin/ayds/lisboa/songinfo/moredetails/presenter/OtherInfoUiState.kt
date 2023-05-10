package ayds.lisboa.songinfo.moredetails.presenter

const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

data class OtherInfoUiState(
    val artistInfoBioContent: String = "",
    val artistInfoUrl: String = "",
    val lastFmDefaultImage: String = LAST_FM_DEFAULT_IMAGE,
)