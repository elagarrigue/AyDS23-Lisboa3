package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

data class OtherInfoUiState(
    val lastFmCard: Card,
    val newYorkTimesCard: Card,
    val wikipediaCard: Card
)