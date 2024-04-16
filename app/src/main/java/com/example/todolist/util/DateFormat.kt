package com.example.todolist.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

fun convertStringToDate(dateString: String, format: String): Date? {
    val parser = SimpleDateFormat(format, Locale.US)
    return try {
        parser.parse(dateString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun formatDate(date: Date?, format: String): String? {
    if (date == null) return null
    val formatter = SimpleDateFormat(format, Locale.US)
    return formatter.format(date)
}

fun convertFormat(dateString: String, inputFormat: String, outputFormat: String): String? {
    val date = convertStringToDate(dateString, inputFormat)
    return formatDate(date, outputFormat)
}

fun convertToDateTime(dateString: String): Pair<LocalDate, LocalTime> {
    val formatter = DateTimeFormatter.ofPattern(Const.currentFormat, Locale.US)
    val dateTime = LocalDateTime.parse(dateString, formatter)
    return Pair(dateTime.toLocalDate(), dateTime.toLocalTime())
}

fun stringToDateTimeInCurrentTimeZone(text: String): Date? {
    val formatter = SimpleDateFormat(Const.currentFormat, Locale.US)
    formatter.timeZone =
        TimeZone.getTimeZone(Const.currentTimeZone) // Set timezone to Vietnam (GMT+7)
    return formatter.parse(text)
}

fun getSecondsFromCurrentToCurrentTimeZone(text: String): Long {
    val dateTime = stringToDateTimeInCurrentTimeZone(text) ?: return -1
    val calendar = Calendar.getInstance(TimeZone.getTimeZone(Const.currentTimeZone))
    val currentTime = calendar.time
    val diff = dateTime.time - currentTime.time
    if (diff < 0) return -1
    // Convert milliseconds to seconds
    return diff / 1000
}