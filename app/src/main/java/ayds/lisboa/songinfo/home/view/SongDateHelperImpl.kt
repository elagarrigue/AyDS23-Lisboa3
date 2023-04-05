package ayds.lisboa.songinfo.home.view

interface SongDateHelper {
    fun getFormat(): String
}

class SongDateHelperImpl(private val releaseDate: String, private val releaseDatePrecision: String): SongDateHelper {
    override fun getFormat(): String {
        TODO("Not yet implemented")
    }
}