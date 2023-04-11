package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.UtilsInjector

object DateCalculatorFactory{

    fun get(releaseDate: String,releaseDatePrecision: String) {
        when (releaseDatePrecision){
            "year" -> YearCalculator(releaseDate,releaseDatePrecision)
            "month"-> MonthCalculator(releaseDate,releaseDatePrecision)
            "day" -> DayCalculator(releaseDate,releaseDatePrecision)
        }
    }
}

sealed class DateCalculator (
    val releaseDate: String,
    val releaseDatePrecision: String
){
    abstract fun getDate() : String

}

class YearCalculator ( releaseDate: String, releaseDatePrecision: String): DateCalculator(releaseDate,releaseDatePrecision){

    override fun getDate(): String{
        val date  = releaseDate.split("-")
        val leapYear = UtilsInjector.leapYear.isLeapYear(date.component1().toInt())
        return date.component1() + " " + (if(leapYear) "(leap year)" else "(not a leap year)")
    }
}

class MonthCalculator( releaseDate: String, releaseDatePrecision: String): DateCalculator(releaseDate,releaseDatePrecision){

    override fun getDate(): String {
        val date  = releaseDate.split("-")
        return getMonth(date.component2()) + ", " +  date.component1()
    }
}

class DayCalculator ( releaseDate: String, releaseDatePrecision: String): DateCalculator(releaseDate,releaseDatePrecision){

    override fun getDate(): String {
        val date  = releaseDate.split("-")
        return date.component3() + "/" + date.component2() + "/" + date.component1()
    }
}
