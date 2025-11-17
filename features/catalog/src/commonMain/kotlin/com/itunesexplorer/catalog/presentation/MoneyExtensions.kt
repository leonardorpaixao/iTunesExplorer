package com.itunesexplorer.catalog.presentation

import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.currency.domain.CurrencyFormatter

/**
 * Presentation layer extension for formatting Money value objects.
 * Converts domain Money to user-friendly formatted strings.
 */
internal fun Money.format(): String {
    return CurrencyFormatter.format(amount, currencyCode)
}
