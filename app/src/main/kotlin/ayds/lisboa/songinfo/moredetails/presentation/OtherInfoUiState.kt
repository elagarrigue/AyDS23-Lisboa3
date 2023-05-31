package ayds.lisboa.songinfo.moredetails.presentation

data class OtherInfoUiState(
    val artistCards: List<ArtistCardState>
)

data class ArtistCardState(
    val descriptionFormatted: String = "",
    val infoUrl: String = "",
    val title: String = "",
    val sourceLogo: String = "",
)