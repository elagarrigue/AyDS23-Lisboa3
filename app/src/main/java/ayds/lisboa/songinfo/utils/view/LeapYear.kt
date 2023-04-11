package ayds.lisboa.songinfo.utils.view


interface LeapYear {
    fun isLeapYear(year: Int): Boolean
}

internal class LeapYearImpl(): LeapYear {
    override fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
    }
}