package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface SourceFactory {
    fun get(source: Source): String
}

internal object SourceFactoryImpl: SourceFactory{
    private const val sourceText = "Source: "

    override fun get(source: Source): String {
        return when(source){
            Source.LastFm -> sourceText + "LastFm"
            Source.NewYorkTimes -> sourceText + "New York Times"
            else -> sourceText + "Wikipedia"
        }
    }
}