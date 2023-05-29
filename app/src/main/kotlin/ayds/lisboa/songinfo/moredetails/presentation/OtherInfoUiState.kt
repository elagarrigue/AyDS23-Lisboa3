package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card.ArtistCard

data class OtherInfoUiState(
    val artistCards: List<ArtistCard>
)