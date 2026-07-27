package com.dfcoding.keepconsistent.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)


@OptIn(ExperimentalTime::class)
fun formatPickedDate(epochMillis: Long): String {
    // DatePickerState reports UTC midnight — convert with UTC to avoid an off-by-one day shift
    val date = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.UTC).date
    val month = date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "${date.dayOfMonth} $month ${date.year}"
}

fun formatPickedTime(hour: Int, minute: Int): String {
    val hourStr = if (hour < 10) "0$hour" else "$hour"
    val minuteStr = if (minute < 10) "0$minute" else "$minute"
    return "${hourStr}:${minuteStr}h"
}