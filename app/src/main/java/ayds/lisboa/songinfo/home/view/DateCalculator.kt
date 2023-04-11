package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.UtilsInjector

object DateCalculatorFactory{

    fun get(releaseDate: String,releaseDatePrecision: String): DateCalculator = when (releaseDatePrecision){
            "year" -> YearCalculator(releaseDate)
            "month"-> MonthCalculator(releaseDate)
            else  -> DayCalculator(releaseDate)
        }

}


sealed class DateCalculator (
    val releaseDate: String,
){
    abstract fun getDate() : String

}

class YearCalculator ( releaseDate: String): DateCalculator(releaseDate){

    override fun getDate(): String{
        val date  = releaseDate.split("-")
        val leapYear = UtilsInjector.leapYear.isLeapYear(date.component1().toInt())
        return date.component1() + " " + (if(leapYear) "(leap year)" else "(not a leap year)")
    }
}

class MonthCalculator( releaseDate: String): DateCalculator(releaseDate){

    override fun getDate(): String {
        val date  = releaseDate.split("-")
        return UtilsInjector.month.getMonth(date.component2().toInt()) + ", " +  date.component1()
    }
}

class DayCalculator ( releaseDate: String): DateCalculator(releaseDate){

    override fun getDate(): String {
        val date  = releaseDate.split("-")
        return date.component3() + "/" + date.component2() + "/" + date.component1()
    }
}
