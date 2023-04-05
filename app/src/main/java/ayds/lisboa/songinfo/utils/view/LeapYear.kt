package ayds.lisboa.songinfo.utils.view


interface LeapYear {
    fun isLeapYear(): String
}

class LeapYearImpl(private val year: Int): LeapYear {
    override fun isLeapYear(): String {
        return if((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) "(leap year)" else "(not a leap year)"
    }
}