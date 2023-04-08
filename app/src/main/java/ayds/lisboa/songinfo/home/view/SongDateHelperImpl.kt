package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.view.LeapYearImpl
import ayds.lisboa.songinfo.utils.view.LeapYear

interface SongDateHelper {
    fun getFormat(): String
}

class SongDateHelperImpl(private val releaseDate: String, private val releaseDatePrecision: String): SongDateHelper {

    private val date  = releaseDate.split("-")
    override fun getFormat(): String {

        var result = ""
        when (releaseDatePrecision) {
            "year" ->  result = getYearFormat()
            "month"-> result =  getMonthFormat()
            "day" -> result =  getDayFormat()
        }
        return result
    }
    private fun getMonth(Month: String):String
    {
        var result = ""
        when(Month) {
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
        val leapYear: LeapYear = LeapYearImpl(date.component1().toInt())
        return date.component1() + " " + (if(leapYear.isLeapYear()) "(leap year)" else "(not a leap year)")
    }

    private fun getMonthFormat():String{
        return getMonth(date.component2()) + ", " +  date.component1()
    }

    private fun getDayFormat():String{
        return date.component3() + "/" + date.component2() + "/" + date.component1()
    }
}