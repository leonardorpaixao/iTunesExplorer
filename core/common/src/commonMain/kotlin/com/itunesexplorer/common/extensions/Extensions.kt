package com.itunesexplorer.common.extensions

import com.itunesexplorer.currency.domain.CurrencyFormatter
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

/**
 * Format a price with currency code.
 * Uses the CurrencyFormatter to properly format prices according to currency rules.
 *
 * @param currencyCode ISO 4217 currency code (e.g., "USD", "EUR", "JPY")
 * @return Formatted price string with proper symbol and decimals
 */
fun Double.toFormattedPrice(currencyCode: String = "USD"): String {
    return CurrencyFormatter.format(this, currencyCode)
}

fun String?.orEmpty(): String = this ?: ""

fun String?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()
