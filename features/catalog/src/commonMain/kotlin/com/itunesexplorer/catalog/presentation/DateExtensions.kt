package com.itunesexplorer.catalog.presentation

import com.itunesexplorer.catalog.presentation.i18n.CatalogStrings
import kotlinx.datetime.LocalDate

/**
 * Presentation layer extension for formatting ISO date strings.
 * Converts ISO date strings (e.g., "2024-01-15T00:00:00Z") to user-friendly formatted dates
 * with localized month names (e.g., "15 de janeiro de 2024").
 */
internal fun String.formatReleaseDate(strings: CatalogStrings): String {
    return try {
        // Parse the date (handles both "YYYY-MM-DD" and "YYYY-MM-DDTHH:MM:SSZ" formats)
        val datePart = this.substringBefore("T")
        val date = LocalDate.parse(datePart)

        // Get the localized month name
        val monthName = when (date.monthNumber) {
            1 -> strings.january
            2 -> strings.february
            3 -> strings.march
            4 -> strings.april
            5 -> strings.may
            6 -> strings.june
            7 -> strings.july
            8 -> strings.august
            9 -> strings.september
            10 -> strings.october
            11 -> strings.november
            12 -> strings.december
            else -> date.monthNumber.toString() // Fallback (should never happen)
        }

        // Format as "DD de MMMM de YYYY"
        "${date.dayOfMonth} de $monthName de ${date.year}"
    } catch (e: Exception) {
        // Fallback to original format if parsing fails
        this.substringBefore("T")
    }
}
