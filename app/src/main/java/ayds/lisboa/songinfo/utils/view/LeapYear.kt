package ayds.lisboa.songinfo.utils.view


interface LeapYear {
    fun isLeapYear(): Boolean
}

class LeapYearImpl(private val year: Int): LeapYear {
    override fun isLeapYear(): Boolean {
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
    }
}