package com.itunesexplorer.common.extensions

import kotlinx.datetime.*

fun String.toFormattedDate(): String {
    return try {
        val instant = Instant.parse(this)
        val timeZone = TimeZone.currentSystemDefault()
        val localDateTime = instant.toLocalDateTime(timeZone)
        "${localDateTime.dayOfMonth}/${localDateTime.monthNumber}/${localDateTime.year}"
    } catch (e: Exception) {
        this
    }
}

fun Long.toFormattedDuration(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        else -> "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    }
}

fun Double.toFormattedPrice(currency: String = "$"): String {
    val rounded = (this * 100).toLong() / 100.0
    val intPart = rounded.toLong()
    val decimalPart = ((rounded - intPart) * 100).toInt()
    return "$currency$intPart.${decimalPart.toString().padStart(2, '0')}"
}

fun String?.orEmpty(): String = this ?: ""

fun String?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()
