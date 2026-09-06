package ir.adicom.mymoney.utils


import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    /**
     * تبدیل Timestamp (میلادی) به تاریخ شمسی
     * مثال: 1704067200000 → "1402/09/15"
     */
    fun convertTimestampToShamsi(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        val gy = calendar.get(Calendar.YEAR)
        val gm = calendar.get(Calendar.MONTH) + 1
        val gd = calendar.get(Calendar.DAY_OF_MONTH)

        val jy = gy - 1600
        val jm: Int
        val jd: Int

        val gDayNo = 365 * gy + ((gy + 3) / 4) - ((gy + 99) / 100) + ((gy + 399) / 400)
        val gMonthDayNo = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)[gm - 1]

        var jDayNo: Int
        val isGLeapYear = ((gy % 4 == 0) && (gy % 100 != 0)) || (gy % 400 == 0)

        val gDayOfYear = gMonthDayNo + gd
        if (isGLeapYear) {
            jDayNo = gDayNo + gDayOfYear - 79
        } else {
            jDayNo = gDayNo + gDayOfYear - 80
        }

        val jy2 = -1595 + 33 * (jDayNo / 12053)
        jDayNo %= 12053

        val jy3 = 4 * (jDayNo / 1461)
        jDayNo %= 1461

        val isJLeapYear: Boolean
        if (jDayNo >= 366) {
            jDayNo--
            isJLeapYear = false
        } else {
            isJLeapYear = true
        }

        val jm2 = (jDayNo / 31)
        val jd2 = (jDayNo % 31) + 1

        val resultJy = jy2 + jy3 + 1595 + 1
        val resultJm = jm2 + 1
        val resultJd = jd2

        return String.format("%04d/%02d/%02d", resultJy, resultJm, resultJd)
    }

    /**
     * تبدیل Timestamp به فرمت شمسی با نام ماه
     * مثال: 1704067200000 → "15 دی 1402"
     */
    fun convertTimestampToShamsiWithMonthName(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        val shamsiDate = convertTimestampToShamsi(timestamp)
        val parts = shamsiDate.split("/")
        if (parts.size != 3) return shamsiDate

        val day = parts[2]
        val monthIndex = parts[1].toIntOrNull()?.minus(1) ?: 0
        val year = parts[0]

        val months = listOf(
            "فروردین", "اردیبهشت", "خرداد", "تیر",
            "مرداد", "شهریور", "مهر", "آبان",
            "آذر", "دی", "بهمن", "اسفند"
        )

        val monthName = if (monthIndex in 0..11) months[monthIndex] else "نامشخص"
        return "$day $monthName $year"
    }

    /**
     * بدست آوردن تاریخ جاری به صورت شمسی
     */
    fun getCurrentDateShamsi(): String {
        return convertTimestampToShamsi(System.currentTimeMillis())
    }

    /**
     * تبدیل Timestamp به نام ماه و سال شمسی
     * مثال: 1704067200000 → "دی 1402"
     */
    fun getMonthYearShamsi(timestamp: Long): String {
        val shamsiDate = convertTimestampToShamsi(timestamp)
        val parts = shamsiDate.split("/")
        if (parts.size != 3) return shamsiDate

        val monthIndex = parts[1].toIntOrNull()?.minus(1) ?: 0
        val year = parts[0]

        val months = listOf(
            "فروردین", "اردیبهشت", "خرداد", "تیر",
            "مرداد", "شهریور", "مهر", "آبان",
            "آذر", "دی", "بهمن", "اسفند"
        )

        val monthName = if (monthIndex in 0..11) months[monthIndex] else "نامشخص"
        return "$monthName $year"
    }

    /**
     * بدست آوردن ابتدای ماه (Timestamp)
     * مثال: ابتدای ماه فروردین 1402
     */
    fun getMonthStartTimestamp(year: Int, month: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * بدست آوردن انتهای ماه (Timestamp)
     * مثال: انتهای ماه فروردین 1402
     */
    fun getMonthEndTimestamp(year: Int, month: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 0, 23, 59, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }
}