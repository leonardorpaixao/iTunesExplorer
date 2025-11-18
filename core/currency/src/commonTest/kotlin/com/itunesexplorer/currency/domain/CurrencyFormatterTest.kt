package com.itunesexplorer.currency.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for CurrencyFormatter.
 * Tests various currency formatting scenarios including:
 * - Different currency codes and their symbols
 * - Decimal precision rules (e.g., JPY has 0 decimals, USD has 2)
 * - Symbol positioning (before or after price)
 * - Thousand separators
 * - Edge cases (zero prices, large numbers, null handling)
 */
class CurrencyFormatterTest {

    @Test
    fun `format should format USD correctly with symbol`() {
        val result = CurrencyFormatter.format(12.99, "USD")
        assertEquals("$12.99", result)
    }

    @Test
    fun `format should format EUR correctly with symbol after price`() {
        val result = CurrencyFormatter.format(10.50, "EUR")
        assertEquals("10.50 €", result)
    }

    @Test
    fun `format should format GBP correctly`() {
        val result = CurrencyFormatter.format(15.00, "GBP")
        assertEquals("£15.00", result)
    }

    @Test
    fun `format should format JPY with zero decimals`() {
        val result = CurrencyFormatter.format(1250.0, "JPY")
        assertEquals("¥1,250", result)
    }

    @Test
    fun `format should format BRL correctly`() {
        val result = CurrencyFormatter.format(99.90, "BRL")
        assertEquals("R$99.90", result)
    }

    @Test
    fun `format should format CHF with symbol after price`() {
        val result = CurrencyFormatter.format(25.50, "CHF")
        assertEquals("25.50 CHF", result)
    }

    @Test
    fun `format should handle zero price`() {
        val result = CurrencyFormatter.format(0.0, "USD")
        assertEquals("$0.00", result)
    }

    @Test
    fun `format should handle large numbers with thousand separators`() {
        val result = CurrencyFormatter.format(1234567.89, "USD")
        assertEquals("$1,234,567.89", result)
    }

    @Test
    fun `format should round to correct decimal places for USD`() {
        val result = CurrencyFormatter.format(12.996, "USD")
        assertEquals("$13.00", result)
    }

    @Test
    fun `format should round to zero decimals for JPY`() {
        val result = CurrencyFormatter.format(1250.7, "JPY")
        assertEquals("¥1,251", result)
    }

    @Test
    fun `format should handle unsupported currency with fallback`() {
        val result = CurrencyFormatter.format(10.50, "XYZ")
        assertTrue(result.contains("10.50"))
    }

    @Test
    fun `format should respect showSymbol parameter`() {
        val resultWithSymbol = CurrencyFormatter.format(12.99, "USD", showSymbol = true)
        val resultWithoutSymbol = CurrencyFormatter.format(12.99, "USD", showSymbol = false)

        assertEquals("$12.99", resultWithSymbol)
        assertEquals("12.99", resultWithoutSymbol)
    }

    @Test
    fun `format should respect showCode parameter`() {
        val result = CurrencyFormatter.format(12.99, "USD", showSymbol = true, showCode = true)
        assertEquals("$12.99 USD", result)
    }

    @Test
    fun `format should format KRW with zero decimals`() {
        val result = CurrencyFormatter.format(50000.0, "KRW")
        assertEquals("₩50,000", result)
    }

    @Test
    fun `format should format INR correctly`() {
        val result = CurrencyFormatter.format(999.99, "INR")
        assertEquals("₹999.99", result)
    }

    @Test
    fun `format should be case-insensitive for currency codes`() {
        val upper = CurrencyFormatter.format(12.99, "USD")
        val lower = CurrencyFormatter.format(12.99, "usd")
        val mixed = CurrencyFormatter.format(12.99, "UsD")

        assertEquals(upper, lower)
        assertEquals(upper, mixed)
    }

    @Test
    fun `format should handle small decimal values`() {
        val result = CurrencyFormatter.format(0.99, "USD")
        assertEquals("$0.99", result)
    }

    @Test
    fun `format should format thousand separator correctly for different amounts`() {
        assertEquals("$1,000.00", CurrencyFormatter.format(1000.0, "USD"))
        assertEquals("$10,000.00", CurrencyFormatter.format(10000.0, "USD"))
        assertEquals("$100,000.00", CurrencyFormatter.format(100000.0, "USD"))
    }

    @Test
    fun `format should handle CLP with zero decimals`() {
        val result = CurrencyFormatter.format(5000.0, "CLP")
        assertEquals("CL$5,000", result)
    }

    @Test
    fun `format should handle IDR with zero decimals`() {
        val result = CurrencyFormatter.format(150000.0, "IDR")
        assertEquals("Rp150,000", result)
    }

    @Test
    fun `format should handle VND with zero decimals`() {
        val result = CurrencyFormatter.format(250000.0, "VND")
        assertEquals("₫250,000", result)
    }
}
