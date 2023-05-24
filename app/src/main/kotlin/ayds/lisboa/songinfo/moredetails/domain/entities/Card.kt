package ayds.lisboa.songinfo.moredetails.domain.entities

data class Card (
    var description: String = "",
    var infoUrl: String = "",
    var source: Source,
    var sourceLogo: String = "",
    var isLocallyStored: Boolean = false
)

enum class Source {
    LastFm,
    Wikipedia,
    NewYorkTimes
}