package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.UtilsInjector

import ayds.lisboa.songinfo.home.view.DateCalculator

interface SongDateHelper {
    fun getFormat(releaseDate: String, releaseDatePrecision: String): String
}

internal class SongDateHelperImpl(): SongDateHelper {
    override fun getFormat(releaseDate: String, releaseDatePrecision: String): String {
        var calculator = DateCalculatorFactory.get(releaseDate, releaseDatePrecision)
        return calculator.getDate()
    }

}