package com.itunesexplorer.currency.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Unit tests for SupportedCurrencies and Currency data class.
 * Tests the currency catalog and lookup functionality.
 */
class SupportedCurrenciesTest {

    // Currency Data Class Tests
    @Test
    fun `Currency should store code symbol name and decimals`() {
        val currency = Currency(
            code = "USD",
            symbol = "$",
            name = "US Dollar",
            decimals = 2
        )

        assertEquals("USD", currency.code)
        assertEquals("$", currency.symbol)
        assertEquals("US Dollar", currency.name)
        assertEquals(2, currency.decimals)
    }

    @Test
    fun `Currency should have default decimals of 2`() {
        val currency = Currency(
            code = "EUR",
            symbol = "€",
            name = "Euro"
        )

        assertEquals(2, currency.decimals)
    }

    // Zero-Decimal Currencies Tests
    @Test
    fun `SupportedCurrencies should have KRW with zero decimals`() {
        assertEquals(0, SupportedCurrencies.KRW.decimals)
    }

    @Test
    fun `SupportedCurrencies should have CLP with zero decimals`() {
        assertEquals(0, SupportedCurrencies.CLP.decimals)
    }

    @Test
    fun `SupportedCurrencies should have IDR with zero decimals`() {
        assertEquals(0, SupportedCurrencies.IDR.decimals)
    }

    @Test
    fun `SupportedCurrencies should have VND with zero decimals`() {
        assertEquals(0, SupportedCurrencies.VND.decimals)
    }

    @Test
    fun `SupportedCurrencies should have HUF with zero decimals`() {
        assertEquals(0, SupportedCurrencies.HUF.decimals)
    }

    @Test
    fun `SupportedCurrencies should have COP with zero decimals`() {
        assertEquals(0, SupportedCurrencies.COP.decimals)
    }

    // getByCode Tests
    @Test
    fun `getByCode should return USD for 'USD'`() {
        val currency = SupportedCurrencies.getByCode("USD")
        assertNotNull(currency)
        assertEquals("USD", currency.code)
    }

    @Test
    fun `getByCode should return EUR for 'EUR'`() {
        val currency = SupportedCurrencies.getByCode("EUR")
        assertNotNull(currency)
        assertEquals("EUR", currency.code)
    }

    @Test
    fun `getByCode should be case-insensitive`() {
        val upper = SupportedCurrencies.getByCode("USD")
        val lower = SupportedCurrencies.getByCode("usd")
        val mixed = SupportedCurrencies.getByCode("UsD")

        assertNotNull(upper)
        assertNotNull(lower)
        assertNotNull(mixed)
        assertEquals(upper.code, lower?.code)
        assertEquals(upper.code, mixed?.code)
    }

    @Test
    fun `getByCode should return null for unknown currency`() {
        val currency = SupportedCurrencies.getByCode("XYZ")
        assertNull(currency)
    }

    @Test
    fun `getByCode should return null for empty string`() {
        val currency = SupportedCurrencies.getByCode("")
        assertNull(currency)
    }

    // Specific Currency Tests
    @Test
    fun `CAD should be Canadian Dollar`() {
        val cad = SupportedCurrencies.getByCode("CAD")
        assertNotNull(cad)
        assertEquals("CA$", cad.symbol)
        assertEquals("Canadian Dollar", cad.name)
    }

    @Test
    fun `AUD should be Australian Dollar`() {
        val aud = SupportedCurrencies.getByCode("AUD")
        assertNotNull(aud)
        assertEquals("AU$", aud.symbol)
        assertEquals("Australian Dollar", aud.name)
    }

    @Test
    fun `CHF should be Swiss Franc`() {
        val chf = SupportedCurrencies.getByCode("CHF")
        assertNotNull(chf)
        assertEquals("CHF", chf.symbol)
        assertEquals("Swiss Franc", chf.name)
    }
}
