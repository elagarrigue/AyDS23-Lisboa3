package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.view.LeapYearImpl
import ayds.lisboa.songinfo.utils.view.LeapYear

interface SongDateHelper {
    fun getFormat(): String
}

class SongDateHelperImpl(private val releaseDate: String, private val releaseDatePrecision: String): SongDateHelper {

    val date  = releaseDate.split("-")
    override fun getFormat(): String {

        var result:String = ""
        when (releaseDatePrecision) {
            "year" ->  result = getYearFormat()
            "month"-> result =  getMonthFormat()
            "day" -> result =  getDayFormat()
        }
        return result
    }
    private fun getMonth(Month: String):String
    {
        var result:String =""
        when(Month) {
            "1"->result = "January"
            "2"->result = "February"
            "3"->result = "March"
            "4"->result = "April"
            "5"->result = "May"
            "6"->result = "June"
            "7"->result = "July"
            "8"->result = "August"
            "9"->result = "September"
            "10"->result = "October"
            "11"->result = "November"
            "12"->result = "December"
        }
        return result
    }

    private fun getYearFormat():String{
        var leapYear:LeapYear = LeapYearImpl(date.component1().toInt())
        return date.component1() + " " + leapYear.isLeapYear()
    }

    private fun getMonthFormat():String{
        return getMonth(date.component2()) + ", " +  date.component1()
    }

    private fun getDayFormat():String{
        return date.component3() + "/" + date.component2() + "/" + date.component1()
    }
}