package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.UtilsInjector

interface SongDateHelper {
    fun getFormat(releaseDate: String, releaseDatePrecision: String): String
}

internal class SongDateHelperImpl(): SongDateHelper {
    override fun getFormat(releaseDate: String, releaseDatePrecision: String): String {
        var result = ""
        when (releaseDatePrecision) {
            "year" ->  result = getYearFormat()
            "month"-> result =  getMonthFormat()
            "day" -> result =  getDayFormat()
        }
        return result
    }
    private fun getMonth(month: String):String
    {
        var result = ""
        when(month) {
            "01"->result = "January"
            "02"->result = "February"
            "03"->result = "March"
            "04"->result = "April"
            "05"->result = "May"
            "06"->result = "June"
            "07"->result = "July"
            "08"->result = "August"
            "09"->result = "September"
            "10"->result = "October"
            "11"->result = "November"
            "12"->result = "December"
        }
        return result
    }

    private fun getYearFormat():String{
        val date  = releaseDate.split("-")
        val leapYear = UtilsInjector.leapYear.isLeapYear(date.component1().toInt())
        return date.component1() + " " + (if(leapYear) "(leap year)" else "(not a leap year)")
    }

    private fun getMonthFormat():String{
        private val date  = releaseDate.split("-")
        return getMonth(date.component2()) + ", " +  date.component1()
    }

    private fun getDayFormat():String{
        private val date  = releaseDate.split("-")
        return date.component3() + "/" + date.component2() + "/" + date.component1()
    }
}