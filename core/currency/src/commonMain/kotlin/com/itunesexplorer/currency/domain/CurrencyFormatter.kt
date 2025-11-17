package com.itunesexplorer.currency.domain

import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Formats prices with currency symbols and proper decimal precision.
 * Handles different currency formatting rules across various locales.
 */
object CurrencyFormatter {

    /**
     * Format a price with its currency symbol and proper decimal places.
     *
     * @param price The price value as a Double
     * @param currencyCode ISO 4217 currency code (e.g., "USD", "EUR", "JPY")
     * @param showSymbol Whether to include the currency symbol (default: true)
     * @param showCode Whether to include the currency code (default: false)
     * @return Formatted price string (e.g., "$12.99", "€10.50", "¥1,250")
     */
    fun format(
        price: Double,
        currencyCode: String,
        showSymbol: Boolean = true,
        showCode: Boolean = false
    ): String {
        val currency = SupportedCurrencies.getByCode(currencyCode)
            ?: return formatFallback(price, currencyCode, showCode)

        // Round price to the correct number of decimals
        val roundedPrice = roundToDecimals(price, currency.decimals)

        // Format the numeric value
        val formattedNumber = formatNumber(roundedPrice, currency)

        // Build the final string with symbol and/or code
        return buildFormattedString(formattedNumber, currency, currencyCode, showSymbol, showCode)
    }

    /**
     * Format a price with automatic currency detection from country code.
     *
     * @param price The price value
     * @param countryCode ISO 3166-1 alpha-2 country code (e.g., "US", "BR")
     * @param showSymbol Whether to include the currency symbol
     * @param showCode Whether to include the currency code
     * @return Formatted price string
     */
    fun formatWithCountry(
        price: Double,
        countryCode: String,
        showSymbol: Boolean = true,
        showCode: Boolean = false
    ): String {
        val currencyCode = CurrencyMapper.getCurrencyForCountry(countryCode) ?: "USD"
        return format(price, currencyCode, showSymbol, showCode)
    }

    /**
     * Format a nullable price, returning a default string if null.
     *
     * @param price The price value (nullable)
     * @param currencyCode ISO 4217 currency code
     * @param defaultValue String to return if price is null (default: "—")
     * @param showSymbol Whether to include the currency symbol
     * @param showCode Whether to include the currency code
     * @return Formatted price string or default value
     */
    fun formatOrDefault(
        price: Double?,
        currencyCode: String?,
        defaultValue: String = "—",
        showSymbol: Boolean = true,
        showCode: Boolean = false
    ): String {
        if (price == null || currencyCode == null) return defaultValue
        return format(price, currencyCode, showSymbol, showCode)
    }

    /**
     * Round a price to the specified number of decimal places.
     */
    private fun roundToDecimals(value: Double, decimals: Int): Double {
        if (decimals == 0) return value.roundToInt().toDouble()
        val multiplier = 10.0.pow(decimals)
        return (value * multiplier).roundToInt() / multiplier
    }

    /**
     * Format a number with thousand separators and decimal points.
     */
    private fun formatNumber(value: Double, currency: Currency): String {
        val integerPart = value.toLong()
        val decimalPart = ((value - integerPart) * 10.0.pow(currency.decimals)).roundToInt()

        // Format integer part with thousand separators
        val formattedInteger = formatWithThousandSeparator(integerPart)

        // Return with or without decimal part
        return if (currency.decimals > 0 && decimalPart > 0) {
            val decimalStr = decimalPart.toString().padStart(currency.decimals, '0')
            "$formattedInteger.$decimalStr"
        } else if (currency.decimals > 0) {
            // Show .00 for currencies that use decimals
            val zeros = "0".repeat(currency.decimals)
            "$formattedInteger.$zeros"
        } else {
            formattedInteger
        }
    }

    /**
     * Format a number with thousand separators (e.g., 1000 -> 1,000).
     */
    private fun formatWithThousandSeparator(value: Long): String {
        val str = value.toString()
        if (str.length <= 3) return str

        val result = StringBuilder()
        var count = 0

        for (i in str.length - 1 downTo 0) {
            if (count == 3) {
                result.insert(0, ',')
                count = 0
            }
            result.insert(0, str[i])
            count++
        }

        return result.toString()
    }

    /**
     * Build the formatted string with symbol and/or code.
     */
    private fun buildFormattedString(
        formattedNumber: String,
        currency: Currency,
        currencyCode: String,
        showSymbol: Boolean,
        showCode: Boolean
    ): String {
        return buildString {
            if (showSymbol) {
                // Most currencies have symbol before the number
                if (shouldSymbolComeLast(currencyCode)) {
                    append(formattedNumber)
                    append(" ")
                    append(currency.symbol)
                } else {
                    append(currency.symbol)
                    append(formattedNumber)
                }
            } else {
                append(formattedNumber)
            }

            if (showCode) {
                append(" ")
                append(currencyCode)
            }
        }
    }

    /**
     * Determine if the currency symbol should come after the number.
     * Most currencies place symbol before, but some (like EUR in some locales) place it after.
     */
    private fun shouldSymbolComeLast(currencyCode: String): Boolean {
        return when (currencyCode.uppercase()) {
            "EUR", "CHF", "NOK", "SEK", "DKK", "CZK", "RON" -> true
            else -> false
        }
    }

    /**
     * Fallback formatting when currency is not found in supported list.
     */
    private fun formatFallback(price: Double, currencyCode: String, showCode: Boolean): String {
        val formatted = formatNumber(price, Currency(currencyCode, "", "", 2))
        return if (showCode) "$formatted $currencyCode" else formatted
    }

    /**
     * Get just the symbol for a currency code.
     *
     * @param currencyCode ISO 4217 currency code
     * @return Currency symbol, or the code itself if not found
     */
    fun getSymbol(currencyCode: String): String {
        return SupportedCurrencies.getByCode(currencyCode)?.symbol ?: currencyCode
    }

    /**
     * Get the full currency name.
     *
     * @param currencyCode ISO 4217 currency code
     * @return Full currency name, or the code if not found
     */
    fun getName(currencyCode: String): String {
        return SupportedCurrencies.getByCode(currencyCode)?.name ?: currencyCode
    }
}
