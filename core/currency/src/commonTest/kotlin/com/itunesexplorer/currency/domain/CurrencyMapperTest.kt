package com.itunesexplorer.currency.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Unit tests for CurrencyMapper.
 * Tests country-to-currency mappings based on iTunes API research.
 */
class CurrencyMapperTest {

    // Americas Tests
    @Test
    fun `getCurrencyForCountry should return USD for United States`() {
        assertEquals("USD", CurrencyMapper.getCurrencyForCountry("US"))
    }

    @Test
    fun `getCurrencyForCountry should return BRL for Brazil`() {
        assertEquals("BRL", CurrencyMapper.getCurrencyForCountry("BR"))
    }

    @Test
    fun `getCurrencyForCountry should return CAD for Canada`() {
        assertEquals("CAD", CurrencyMapper.getCurrencyForCountry("CA"))
    }

    @Test
    fun `getCurrencyForCountry should return MXN for Mexico`() {
        assertEquals("MXN", CurrencyMapper.getCurrencyForCountry("MX"))
    }

    @Test
    fun `getCurrencyForCountry should return USD for Argentina`() {
        // Argentina uses USD in iTunes, not ARS
        assertEquals("USD", CurrencyMapper.getCurrencyForCountry("AR"))
    }

    @Test
    fun `getCurrencyForCountry should return CLP for Chile`() {
        assertEquals("CLP", CurrencyMapper.getCurrencyForCountry("CL"))
    }

    @Test
    fun `getCurrencyForCountry should return COP for Colombia`() {
        assertEquals("COP", CurrencyMapper.getCurrencyForCountry("CO"))
    }

    // Europe - Eurozone Tests
    @Test
    fun `getCurrencyForCountry should return EUR for Germany`() {
        assertEquals("EUR", CurrencyMapper.getCurrencyForCountry("DE"))
    }

    @Test
    fun `getCurrencyForCountry should return EUR for France`() {
        assertEquals("EUR", CurrencyMapper.getCurrencyForCountry("FR"))
    }

    @Test
    fun `getCurrencyForCountry should return EUR for Spain`() {
        assertEquals("EUR", CurrencyMapper.getCurrencyForCountry("ES"))
    }

    @Test
    fun `getCurrencyForCountry should return EUR for Italy`() {
        assertEquals("EUR", CurrencyMapper.getCurrencyForCountry("IT"))
    }

    @Test
    fun `getCurrencyForCountry should return EUR for Netherlands`() {
        assertEquals("EUR", CurrencyMapper.getCurrencyForCountry("NL"))
    }

    @Test
    fun `getCurrencyForCountry should return EUR for Portugal`() {
        assertEquals("EUR", CurrencyMapper.getCurrencyForCountry("PT"))
    }

    // Europe - Non-Eurozone Tests
    @Test
    fun `getCurrencyForCountry should return GBP for United Kingdom`() {
        assertEquals("GBP", CurrencyMapper.getCurrencyForCountry("GB"))
    }

    @Test
    fun `getCurrencyForCountry should return CHF for Switzerland`() {
        assertEquals("CHF", CurrencyMapper.getCurrencyForCountry("CH"))
    }

    @Test
    fun `getCurrencyForCountry should return SEK for Sweden`() {
        assertEquals("SEK", CurrencyMapper.getCurrencyForCountry("SE"))
    }

    @Test
    fun `getCurrencyForCountry should return NOK for Norway`() {
        assertEquals("NOK", CurrencyMapper.getCurrencyForCountry("NO"))
    }

    @Test
    fun `getCurrencyForCountry should return DKK for Denmark`() {
        assertEquals("DKK", CurrencyMapper.getCurrencyForCountry("DK"))
    }

    @Test
    fun `getCurrencyForCountry should return PLN for Poland`() {
        assertEquals("PLN", CurrencyMapper.getCurrencyForCountry("PL"))
    }

    @Test
    fun `getCurrencyForCountry should return CZK for Czech Republic`() {
        assertEquals("CZK", CurrencyMapper.getCurrencyForCountry("CZ"))
    }

    @Test
    fun `getCurrencyForCountry should return RUB for Russia`() {
        assertEquals("RUB", CurrencyMapper.getCurrencyForCountry("RU"))
    }

    // Asia-Pacific Tests
    @Test
    fun `getCurrencyForCountry should return JPY for Japan`() {
        assertEquals("JPY", CurrencyMapper.getCurrencyForCountry("JP"))
    }

    @Test
    fun `getCurrencyForCountry should return CNY for China`() {
        assertEquals("CNY", CurrencyMapper.getCurrencyForCountry("CN"))
    }

    @Test
    fun `getCurrencyForCountry should return INR for India`() {
        assertEquals("INR", CurrencyMapper.getCurrencyForCountry("IN"))
    }

    @Test
    fun `getCurrencyForCountry should return KRW for South Korea`() {
        assertEquals("KRW", CurrencyMapper.getCurrencyForCountry("KR"))
    }

    @Test
    fun `getCurrencyForCountry should return AUD for Australia`() {
        assertEquals("AUD", CurrencyMapper.getCurrencyForCountry("AU"))
    }

    @Test
    fun `getCurrencyForCountry should return NZD for New Zealand`() {
        assertEquals("NZD", CurrencyMapper.getCurrencyForCountry("NZ"))
    }

    @Test
    fun `getCurrencyForCountry should return SGD for Singapore`() {
        assertEquals("SGD", CurrencyMapper.getCurrencyForCountry("SG"))
    }

    @Test
    fun `getCurrencyForCountry should return THB for Thailand`() {
        assertEquals("THB", CurrencyMapper.getCurrencyForCountry("TH"))
    }

    // Middle East & Africa Tests
    @Test
    fun `getCurrencyForCountry should return AED for UAE`() {
        assertEquals("AED", CurrencyMapper.getCurrencyForCountry("AE"))
    }

    @Test
    fun `getCurrencyForCountry should return ILS for Israel`() {
        assertEquals("ILS", CurrencyMapper.getCurrencyForCountry("IL"))
    }

    @Test
    fun `getCurrencyForCountry should return ZAR for South Africa`() {
        assertEquals("ZAR", CurrencyMapper.getCurrencyForCountry("ZA"))
    }

    // Currency Object Retrieval Tests
    @Test
    fun `getCurrencyObjectForCountry should return Currency object for US`() {
        val currency = CurrencyMapper.getCurrencyObjectForCountry("US")
        assertNotNull(currency)
        assertEquals("USD", currency.code)
        assertEquals("$", currency.symbol)
        assertEquals(2, currency.decimals)
    }

    @Test
    fun `getCurrencyObjectForCountry should return Currency object for JP`() {
        val currency = CurrencyMapper.getCurrencyObjectForCountry("JP")
        assertNotNull(currency)
        assertEquals("JPY", currency.code)
        assertEquals("¥", currency.symbol)
        assertEquals(0, currency.decimals)
    }

    @Test
    fun `getCurrencyObjectForCountry should return null for unknown country`() {
        val currency = CurrencyMapper.getCurrencyObjectForCountry("XX")
        assertNull(currency)
    }

    // Countries for Currency Tests
    @Test
    fun `getCountriesForCurrency should return all EUR countries`() {
        val eurCountries = CurrencyMapper.getCountriesForCurrency("EUR")
        assertTrue(eurCountries.isNotEmpty())
        assertTrue(eurCountries.contains("DE"))
        assertTrue(eurCountries.contains("FR"))
        assertTrue(eurCountries.contains("ES"))
        assertTrue(eurCountries.contains("IT"))
    }

    @Test
    fun `getCountriesForCurrency should return USD countries`() {
        val usdCountries = CurrencyMapper.getCountriesForCurrency("USD")
        assertTrue(usdCountries.isNotEmpty())
        assertTrue(usdCountries.contains("US"))
        assertTrue(usdCountries.contains("AR")) // Argentina uses USD in iTunes
    }

    @Test
    fun `getCountriesForCurrency should be case-insensitive`() {
        val upper = CurrencyMapper.getCountriesForCurrency("USD")
        val lower = CurrencyMapper.getCountriesForCurrency("usd")
        assertEquals(upper.size, lower.size)
    }

    // Mapping Existence Tests
    @Test
    fun `hasMapping should return true for US`() {
        assertTrue(CurrencyMapper.hasMapping("US"))
    }

    @Test
    fun `hasMapping should return true for BR`() {
        assertTrue(CurrencyMapper.hasMapping("BR"))
    }

    @Test
    fun `hasMapping should return false for unknown country`() {
        assertFalse(CurrencyMapper.hasMapping("XX"))
    }

    @Test
    fun `hasMapping should be case-insensitive`() {
        assertTrue(CurrencyMapper.hasMapping("US"))
        assertTrue(CurrencyMapper.hasMapping("us"))
        assertTrue(CurrencyMapper.hasMapping("Us"))
    }

    // All Mappings Test
    @Test
    fun `getAllMappings should return non-empty map`() {
        val mappings = CurrencyMapper.getAllMappings()
        assertTrue(mappings.isNotEmpty())
    }

    @Test
    fun `getAllMappings should contain key countries`() {
        val mappings = CurrencyMapper.getAllMappings()
        assertTrue(mappings.containsKey("US"))
        assertTrue(mappings.containsKey("GB"))
        assertTrue(mappings.containsKey("JP"))
        assertTrue(mappings.containsKey("BR"))
        assertTrue(mappings.containsKey("DE"))
    }

    // Case Insensitivity Tests
    @Test
    fun `getCurrencyForCountry should be case-insensitive`() {
        assertEquals("USD", CurrencyMapper.getCurrencyForCountry("US"))
        assertEquals("USD", CurrencyMapper.getCurrencyForCountry("us"))
        assertEquals("USD", CurrencyMapper.getCurrencyForCountry("Us"))
    }

    // Null/Unknown Tests
    @Test
    fun `getCurrencyForCountry should return null for unknown country`() {
        assertNull(CurrencyMapper.getCurrencyForCountry("XX"))
    }

    @Test
    fun `getCurrencyForCountry should return null for empty string`() {
        assertNull(CurrencyMapper.getCurrencyForCountry(""))
    }

    // Special Cases Tests
    @Test
    fun `getCurrencyForCountry should handle Hong Kong correctly`() {
        assertEquals("HKD", CurrencyMapper.getCurrencyForCountry("HK"))
    }

    @Test
    fun `getCurrencyForCountry should handle Taiwan correctly`() {
        assertEquals("TWD", CurrencyMapper.getCurrencyForCountry("TW"))
    }

    @Test
    fun `getCurrencyForCountry should return USD for Ukraine`() {
        // Ukraine uses USD in iTunes, not UAH
        assertEquals("USD", CurrencyMapper.getCurrencyForCountry("UA"))
    }

    // Edge Cases
    @Test
    fun `getCurrencyForCountry should handle all supported countries from preferences`() {
        // Test all countries listed in SupportedCountries
        val countryTests = mapOf(
            "US" to "USD",
            "BR" to "BRL",
            "CA" to "CAD",
            "MX" to "MXN",
            "AR" to "USD",
            "CL" to "CLP",
            "CO" to "COP",
            "GB" to "GBP",
            "DE" to "EUR",
            "FR" to "EUR",
            "ES" to "EUR",
            "IT" to "EUR",
            "PT" to "EUR",
            "NL" to "EUR",
            "BE" to "EUR",
            "CH" to "CHF",
            "AT" to "EUR",
            "SE" to "SEK",
            "NO" to "NOK",
            "DK" to "DKK",
            "FI" to "EUR",
            "PL" to "PLN",
            "RU" to "RUB",
            "JP" to "JPY",
            "AU" to "AUD",
            "NZ" to "NZD",
            "KR" to "KRW",
            "CN" to "CNY",
            "IN" to "INR",
            "SG" to "SGD",
            "TH" to "THB",
            "AE" to "AED",
            "ZA" to "ZAR"
        )

        countryTests.forEach { (country, expectedCurrency) ->
            assertEquals(
                expectedCurrency,
                CurrencyMapper.getCurrencyForCountry(country),
                "Country $country should map to $expectedCurrency"
            )
        }
    }
}
