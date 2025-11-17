package com.itunesexplorer.currency.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    // Major Currencies Tests
    @Test
    fun `SupportedCurrencies should have USD defined`() {
        assertNotNull(SupportedCurrencies.USD)
        assertEquals("USD", SupportedCurrencies.USD.code)
        assertEquals("$", SupportedCurrencies.USD.symbol)
        assertEquals("US Dollar", SupportedCurrencies.USD.name)
        assertEquals(2, SupportedCurrencies.USD.decimals)
    }

    @Test
    fun `SupportedCurrencies should have EUR defined`() {
        assertNotNull(SupportedCurrencies.EUR)
        assertEquals("EUR", SupportedCurrencies.EUR.code)
        assertEquals("€", SupportedCurrencies.EUR.symbol)
        assertEquals("Euro", SupportedCurrencies.EUR.name)
        assertEquals(2, SupportedCurrencies.EUR.decimals)
    }

    @Test
    fun `SupportedCurrencies should have GBP defined`() {
        assertNotNull(SupportedCurrencies.GBP)
        assertEquals("GBP", SupportedCurrencies.GBP.code)
        assertEquals("£", SupportedCurrencies.GBP.symbol)
        assertEquals(2, SupportedCurrencies.GBP.decimals)
    }

    @Test
    fun `SupportedCurrencies should have JPY with zero decimals`() {
        assertNotNull(SupportedCurrencies.JPY)
        assertEquals("JPY", SupportedCurrencies.JPY.code)
        assertEquals("¥", SupportedCurrencies.JPY.symbol)
        assertEquals(0, SupportedCurrencies.JPY.decimals)
    }

    @Test
    fun `SupportedCurrencies should have BRL defined`() {
        assertNotNull(SupportedCurrencies.BRL)
        assertEquals("BRL", SupportedCurrencies.BRL.code)
        assertEquals("R$", SupportedCurrencies.BRL.symbol)
        assertEquals(2, SupportedCurrencies.BRL.decimals)
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

    // All Currencies List Tests
    @Test
    fun `all should contain all 41 currencies`() {
        assertTrue(SupportedCurrencies.all.size >= 41)
    }

    @Test
    fun `all should contain major currencies`() {
        val codes = SupportedCurrencies.all.map { it.code }
        assertTrue(codes.contains("USD"))
        assertTrue(codes.contains("EUR"))
        assertTrue(codes.contains("GBP"))
        assertTrue(codes.contains("JPY"))
        assertTrue(codes.contains("CNY"))
    }

    @Test
    fun `all should contain Latin American currencies`() {
        val codes = SupportedCurrencies.all.map { it.code }
        assertTrue(codes.contains("BRL"))
        assertTrue(codes.contains("MXN"))
        assertTrue(codes.contains("ARS"))
        assertTrue(codes.contains("CLP"))
        assertTrue(codes.contains("COP"))
    }

    @Test
    fun `all should contain Asian currencies`() {
        val codes = SupportedCurrencies.all.map { it.code }
        assertTrue(codes.contains("INR"))
        assertTrue(codes.contains("KRW"))
        assertTrue(codes.contains("SGD"))
        assertTrue(codes.contains("THB"))
        assertTrue(codes.contains("MYR"))
    }

    @Test
    fun `all should not contain duplicates`() {
        val codes = SupportedCurrencies.all.map { it.code }
        val uniqueCodes = codes.distinct()
        assertEquals(codes.size, uniqueCodes.size, "Currency list should not contain duplicates")
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

    // isSupported Tests
    @Test
    fun `isSupported should return true for USD`() {
        assertTrue(SupportedCurrencies.isSupported("USD"))
    }

    @Test
    fun `isSupported should return true for EUR`() {
        assertTrue(SupportedCurrencies.isSupported("EUR"))
    }

    @Test
    fun `isSupported should return false for unknown currency`() {
        assertFalse(SupportedCurrencies.isSupported("XYZ"))
    }

    @Test
    fun `isSupported should be case-insensitive`() {
        assertTrue(SupportedCurrencies.isSupported("USD"))
        assertTrue(SupportedCurrencies.isSupported("usd"))
        assertTrue(SupportedCurrencies.isSupported("UsD"))
    }

    // Symbol Tests
    @Test
    fun `USD should have dollar symbol`() {
        assertEquals("$", SupportedCurrencies.USD.symbol)
    }

    @Test
    fun `EUR should have euro symbol`() {
        assertEquals("€", SupportedCurrencies.EUR.symbol)
    }

    @Test
    fun `GBP should have pound symbol`() {
        assertEquals("£", SupportedCurrencies.GBP.symbol)
    }

    @Test
    fun `JPY should have yen symbol`() {
        assertEquals("¥", SupportedCurrencies.JPY.symbol)
    }

    @Test
    fun `INR should have rupee symbol`() {
        assertEquals("₹", SupportedCurrencies.INR.symbol)
    }

    @Test
    fun `KRW should have won symbol`() {
        assertEquals("₩", SupportedCurrencies.KRW.symbol)
    }

    // Complete Currency Coverage Tests
    @Test
    fun `all currencies should have non-empty codes`() {
        SupportedCurrencies.all.forEach { currency ->
            assertTrue(currency.code.isNotEmpty(), "Currency code should not be empty")
            assertEquals(3, currency.code.length, "Currency code should be 3 characters: ${currency.code}")
        }
    }

    @Test
    fun `all currencies should have non-empty symbols`() {
        SupportedCurrencies.all.forEach { currency ->
            assertTrue(currency.symbol.isNotEmpty(), "Currency symbol should not be empty for ${currency.code}")
        }
    }

    @Test
    fun `all currencies should have non-empty names`() {
        SupportedCurrencies.all.forEach { currency ->
            assertTrue(currency.name.isNotEmpty(), "Currency name should not be empty for ${currency.code}")
        }
    }

    @Test
    fun `all currencies should have valid decimal values`() {
        SupportedCurrencies.all.forEach { currency ->
            assertTrue(currency.decimals >= 0, "Decimals should be >= 0 for ${currency.code}")
            assertTrue(currency.decimals <= 4, "Decimals should be <= 4 for ${currency.code}")
        }
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
