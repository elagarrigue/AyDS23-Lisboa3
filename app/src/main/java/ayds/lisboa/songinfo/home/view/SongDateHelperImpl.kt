package ayds.lisboa.songinfo.home.view

import app/src/main/java/ayds/lisboa/songinfo/utils/view/LeapYear.kt

interface SongDateHelper {
    fun getFormat(): String
}

class SongDateHelperImpl(private val releaseDate: String, private val releaseDatePrecision: String): SongDateHelper {

    val date : string = releaseDate.split("-")
    override fun getFormat(): String {
        val result:string
        when (releaseDatePrecision) {
            "year" ->  result = date.component1() + " " + isLeapYear()
            "month"-> result = getMonth(date.component2()) + ", " +  date.component1()
            "day" -> result = date.component3() + "/" + date.component2() + "/" + date.component1()
        }
        return result
    }
    private fun getMonth(private val Month: String):String
    {

        val result:string
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
}