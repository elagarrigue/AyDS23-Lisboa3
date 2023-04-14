package ayds.lisboa.songinfo.home.view

interface DateCalculatorFactory{
    fun get(releaseDate: String, releaseDatePrecision: String):DateCalculator
}
internal object DateCalculatorFactoryImpl : DateCalculatorFactory {
    private const val year = "year"
    private const val month = "month"
    private const val day = "day"
    override fun get(releaseDate: String, releaseDatePrecision: String): DateCalculator =
        when (releaseDatePrecision) {
            year -> YearCalculator(releaseDate)
            month -> MonthCalculator(releaseDate)
            day -> DayCalculator(releaseDate)
            else -> DefaultCalculator(releaseDate)
        }
}

sealed class DateCalculator (
    val releaseDate: String,
){
    abstract fun getDate() : String
}

internal class YearCalculator(releaseDate: String): DateCalculator(releaseDate){
    override fun getDate(): String{
        val date  = releaseDate.split("-")
        val year = date.component1()
        val isLeapYear = isLeapYear(year.toInt())
        return year + (if(isLeapYear) " (leap year)" else " (not a leap year)")
    }
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)
    }
}

internal class MonthCalculator(releaseDate: String): DateCalculator(releaseDate) {
    override fun getDate(): String {
        val date = releaseDate.split("-")
        val year = date.component1()
        val month = date.component2()
        val monthName = getMonth(month.toInt())
        return "$monthName, $year"
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

internal class DayCalculator(releaseDate: String): DateCalculator(releaseDate){

    override fun getDate(): String {
        val date  = releaseDate.split("-")
        val year = date.component1()
        val month = date.component2()
        val day = date.component3()
        return "$day/$month/$year"
    }
}

class DefaultCalculator(releaseDate: String): DateCalculator(releaseDate){
    override fun getDate(): String {
        return releaseDate
    }
}