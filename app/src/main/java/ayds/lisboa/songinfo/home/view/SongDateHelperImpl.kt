package ayds.lisboa.songinfo.home.view

interface SongDateHelper {
    fun getFormat(releaseDate: String, releaseDatePrecision: String): String
}

internal class SongDateHelperImpl : SongDateHelper {
    override fun getFormat(releaseDate: String, releaseDatePrecision: String): String {
        val calculator : DateCalculator = DateCalculatorFactory.get(releaseDate, releaseDatePrecision)
        return calculator.getDate()
    }
}