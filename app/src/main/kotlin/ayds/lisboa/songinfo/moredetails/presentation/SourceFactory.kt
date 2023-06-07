package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface SourceFactory {
    fun get(source: Source): String
}

internal object SourceFactoryImpl: SourceFactory{
    private const val LAST_FM_TITLE = "Source: LastFM"
    private const val NEW_YORK_TIMES_TITLE = "Source: NewYorkTimes"
    private const val WIKIPEDIA_TITLE = "Source: Wikipedia"

    override fun get(source: Source): String {
        return when(source){
            Source.LastFm -> LAST_FM_TITLE
            Source.NewYorkTimes -> NEW_YORK_TIMES_TITLE
            Source.Wikipedia -> WIKIPEDIA_TITLE
        }
    }
}