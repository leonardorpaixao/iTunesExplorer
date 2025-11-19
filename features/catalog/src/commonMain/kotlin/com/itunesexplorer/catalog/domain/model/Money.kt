package com.itunesexplorer.catalog.domain.model

import com.itunesexplorer.currency.domain.CurrencyFormatter

/**
 * Value object representing monetary amount with currency.
 * Immutable and contains business rules for money representation.
 */
data class Money(
    val amount: Double,
    val currencyCode: String
) {
    init {
        require(currencyCode.isNotBlank()) { "Currency code cannot be blank" }
    }

    fun format(): String {
        return CurrencyFormatter.format(amount, currencyCode)
    }

    companion object {
        fun fromOptional(amount: Double?, currencyCode: String?): Money? {
            return if (amount != null && currencyCode != null) {
                Money(amount, currencyCode)
            } else {
                null
            }
        }
    }
}
