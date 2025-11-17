package com.itunesexplorer.catalog.domain.model

/**
 * Value object representing monetary amount with currency.
 * Immutable and contains business rules for money representation.
 */
data class Money(
    val amount: Double,
    val currencyCode: String
) {
    init {
        require(amount >= 0) { "Money amount cannot be negative" }
        require(currencyCode.isNotBlank()) { "Currency code cannot be blank" }
    }

    companion object {
        /**
         * Creates a Money instance from optional values.
         * Returns null if either amount or currency is missing.
         */
        fun fromOptional(amount: Double?, currencyCode: String?): Money? {
            return if (amount != null && currencyCode != null) {
                Money(amount, currencyCode)
            } else {
                null
            }
        }
    }
}
