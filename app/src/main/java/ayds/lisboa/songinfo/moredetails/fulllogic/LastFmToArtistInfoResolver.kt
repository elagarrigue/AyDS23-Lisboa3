package ayds.lisboa.songinfo.moredetails.fulllogic

interface LastFmToArtistInfoResolver {
    fun getArtistInfoFromExternalData(serviceData: String?): ArtistInfo?
}