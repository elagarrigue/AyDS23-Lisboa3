package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.utils.UtilsInjector

interface DateCalculatorFactory{
    fun get(releaseDate: String,releaseDatePrecision: String):DateCalculator
}
object DateCalculatorFactoryImpl : DateCalculatorFactory{
    override fun get(releaseDate: String, releaseDatePrecision: String): DateCalculator =
        when (releaseDatePrecision){
            "year" -> YearCalculator(releaseDate)
            "month"-> MonthCalculator(releaseDate)
            "day"  -> DayCalculator(releaseDate)
            else -> DefaultCalculator(releaseDate)
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
        val isLeapYear = isLeapYear(date.component1().toInt())
        return date.component1() + (if(isLeapYear) " (leap year)" else " (not a leap year)")
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
    }
}

class MonthCalculator( releaseDate: String): DateCalculator(releaseDate) {

    override fun getDate(): String {
        val date = releaseDate.split("-")
        val month = getMonth(date.component2().toInt())
        return month + ", " + date.component1()
    }
    private fun getMonth(month: Int): String =
        when(month) {
            1-> "January"
            2-> "February"
            3-> "March"
            4-> "April"
            5-> "May"
            6-> "June"
            7-> "July"
            8-> "August"
            9-> "September"
            10-> "October"
            11-> "November"
            12-> "December"
            else -> "No Month Match"
        }
}

class DayCalculator ( releaseDate: String): DateCalculator(releaseDate){

    override fun getDate(): String {
        val date  = releaseDate.split("-")
        return date.component3() + "/" + date.component2() + "/" + date.component1()
    }
}

class DefaultCalculator(releaseDate: String): DateCalculator(releaseDate){
    override fun getDate(): String {
        return releaseDate
    }
}