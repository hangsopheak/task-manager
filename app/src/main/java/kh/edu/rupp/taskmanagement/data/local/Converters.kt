package kh.edu.rupp.taskmanagement.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

// the database can only hold text and numbers, so the date travels as ISO text
class Converters {
    @TypeConverter
    fun dateToText(date: LocalDate): String = date.toString()

    @TypeConverter
    fun textToDate(text: String): LocalDate = LocalDate.parse(text)
}
