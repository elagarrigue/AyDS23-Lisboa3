package ayds.lisboa.songinfo.moredetails.domain.entities

sealed class Card {
    data class ArtistCard(
        var description: String = "",
        var infoUrl: String = "",
        var source: Int,
        var sourceLogo: String = "",
        var isLocallyStored: Boolean = false
    ) : Card()
    object EmptyCard : Card()
}

enum class Source {
    LastFm,
    Wikipedia,
    NewYorkTimes;

    val position: Int get() = this.ordinal
}
