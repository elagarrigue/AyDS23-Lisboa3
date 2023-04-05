package ayds.lisboa.songinfo.utils.view


interface LeapYear {

    fun isLeapYear(): Boolean
}

class LeapYearImpl(private val year: Int): LeapYear {
    override fun isLeapYear(): Boolean {
        TODO("Not yet implemented")
    }
}