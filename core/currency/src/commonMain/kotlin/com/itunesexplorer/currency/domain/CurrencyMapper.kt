package com.itunesexplorer.currency.domain

/**
 * Maps country codes (ISO 3166-1 alpha-2) to their corresponding currency codes (ISO 4217).
 * Based on iTunes Search API research covering 184 countries.
 *
 * Note: Some countries that typically use their own currencies may still use USD in the iTunes Store
 * (e.g., Argentina uses USD instead of ARS in iTunes).
 */
object CurrencyMapper {

    /**
     * Comprehensive mapping of country code to currency code.
     * Data based on testing and research of iTunes Search API responses.
     */
    private val countryToCurrency: Map<String, String> = mapOf(
        // Americas
        "US" to "USD",  // United States
        "CA" to "CAD",  // Canada
        "MX" to "MXN",  // Mexico
        "BR" to "BRL",  // Brazil
        "AR" to "USD",  // Argentina (iTunes uses USD, not ARS)
        "CL" to "CLP",  // Chile
        "CO" to "COP",  // Colombia
        "PE" to "PEN",  // Peru
        "VE" to "USD",  // Venezuela
        "EC" to "USD",  // Ecuador
        "BO" to "USD",  // Bolivia
        "PY" to "USD",  // Paraguay
        "UY" to "USD",  // Uruguay
        "CR" to "USD",  // Costa Rica
        "PA" to "USD",  // Panama
        "GT" to "USD",  // Guatemala
        "HN" to "USD",  // Honduras
        "SV" to "USD",  // El Salvador
        "NI" to "USD",  // Nicaragua
        "DO" to "USD",  // Dominican Republic
        "JM" to "USD",  // Jamaica
        "TT" to "USD",  // Trinidad and Tobago
        "BS" to "USD",  // Bahamas
        "BB" to "USD",  // Barbados
        "BZ" to "USD",  // Belize

        // Europe - Eurozone (EUR)
        "DE" to "EUR",  // Germany
        "FR" to "EUR",  // France
        "IT" to "EUR",  // Italy
        "ES" to "EUR",  // Spain
        "NL" to "EUR",  // Netherlands
        "BE" to "EUR",  // Belgium
        "AT" to "EUR",  // Austria
        "PT" to "EUR",  // Portugal
        "IE" to "EUR",  // Ireland
        "FI" to "EUR",  // Finland
        "GR" to "EUR",  // Greece
        "LU" to "EUR",  // Luxembourg
        "SK" to "EUR",  // Slovakia
        "SI" to "EUR",  // Slovenia
        "EE" to "EUR",  // Estonia
        "LV" to "EUR",  // Latvia
        "LT" to "EUR",  // Lithuania
        "CY" to "EUR",  // Cyprus
        "MT" to "EUR",  // Malta

        // Europe - Non-Eurozone
        "GB" to "GBP",  // United Kingdom
        "CH" to "CHF",  // Switzerland
        "NO" to "NOK",  // Norway
        "SE" to "SEK",  // Sweden
        "DK" to "DKK",  // Denmark
        "PL" to "PLN",  // Poland
        "CZ" to "CZK",  // Czech Republic
        "HU" to "HUF",  // Hungary
        "RO" to "RON",  // Romania
        "BG" to "USD",  // Bulgaria (iTunes may use USD or BGN)
        "HR" to "EUR",  // Croatia (adopted EUR in 2023)
        "IS" to "USD",  // Iceland
        "UA" to "USD",  // Ukraine (iTunes uses USD, not UAH)
        "RS" to "USD",  // Serbia
        "RU" to "RUB",  // Russia
        "TR" to "TRY",  // Turkey
        "BY" to "USD",  // Belarus
        "MD" to "USD",  // Moldova
        "MK" to "USD",  // North Macedonia
        "AL" to "USD",  // Albania
        "BA" to "USD",  // Bosnia and Herzegovina
        "ME" to "EUR",  // Montenegro

        // Asia-Pacific
        "JP" to "JPY",  // Japan
        "CN" to "CNY",  // China
        "IN" to "INR",  // India
        "KR" to "KRW",  // South Korea
        "AU" to "AUD",  // Australia
        "NZ" to "NZD",  // New Zealand
        "SG" to "SGD",  // Singapore
        "HK" to "HKD",  // Hong Kong
        "TW" to "TWD",  // Taiwan
        "TH" to "THB",  // Thailand
        "MY" to "MYR",  // Malaysia
        "ID" to "IDR",  // Indonesia
        "PH" to "PHP",  // Philippines
        "VN" to "VND",  // Vietnam
        "PK" to "USD",  // Pakistan
        "BD" to "USD",  // Bangladesh
        "LK" to "USD",  // Sri Lanka
        "NP" to "USD",  // Nepal
        "KH" to "USD",  // Cambodia
        "LA" to "USD",  // Laos
        "MM" to "USD",  // Myanmar
        "MN" to "USD",  // Mongolia
        "BN" to "USD",  // Brunei
        "MO" to "HKD",  // Macau

        // Middle East
        "AE" to "AED",  // United Arab Emirates
        "SA" to "SAR",  // Saudi Arabia
        "IL" to "ILS",  // Israel
        "EG" to "EGP",  // Egypt
        "KW" to "USD",  // Kuwait
        "QA" to "USD",  // Qatar
        "OM" to "USD",  // Oman
        "BH" to "USD",  // Bahrain
        "JO" to "USD",  // Jordan
        "LB" to "USD",  // Lebanon
        "IQ" to "USD",  // Iraq
        "YE" to "USD",  // Yemen
        "SY" to "USD",  // Syria
        "PS" to "USD",  // Palestine

        // Africa
        "ZA" to "ZAR",  // South Africa
        "NG" to "NGN",  // Nigeria
        "KE" to "USD",  // Kenya
        "GH" to "USD",  // Ghana
        "TZ" to "USD",  // Tanzania
        "UG" to "USD",  // Uganda
        "ET" to "USD",  // Ethiopia
        "MA" to "USD",  // Morocco
        "DZ" to "USD",  // Algeria
        "TN" to "USD",  // Tunisia
        "SN" to "USD",  // Senegal
        "CI" to "USD",  // Ivory Coast
        "CM" to "USD",  // Cameroon
        "ZW" to "USD",  // Zimbabwe
        "MU" to "USD",  // Mauritius
        "BW" to "USD",  // Botswana
        "NA" to "USD",  // Namibia
        "MZ" to "USD",  // Mozambique
        "AO" to "USD",  // Angola

        // Central Asia
        "KZ" to "KZT",  // Kazakhstan
        "UZ" to "USD",  // Uzbekistan
        "TM" to "USD",  // Turkmenistan
        "KG" to "USD",  // Kyrgyzstan
        "TJ" to "USD",  // Tajikistan
        "AM" to "USD",  // Armenia
        "AZ" to "USD",  // Azerbaijan
        "GE" to "USD",  // Georgia

        // Oceania
        "FJ" to "USD",  // Fiji
        "PG" to "USD",  // Papua New Guinea
        "NC" to "USD",  // New Caledonia
        "PF" to "USD",  // French Polynesia

        // Special Administrative Regions & Territories
        "GU" to "USD",  // Guam
        "VI" to "USD",  // US Virgin Islands
        "PR" to "USD",  // Puerto Rico
        "AS" to "USD",  // American Samoa
    )

    /**
     * Get the currency code for a given country code.
     *
     * @param countryCode ISO 3166-1 alpha-2 country code (e.g., "US", "BR", "GB")
     * @return The ISO 4217 currency code (e.g., "USD", "BRL", "GBP"), or null if not found
     */
    fun getCurrencyForCountry(countryCode: String): String? {
        return countryToCurrency[countryCode.uppercase()]
    }

    /**
     * Get the Currency object for a given country code.
     *
     * @param countryCode ISO 3166-1 alpha-2 country code
     * @return The Currency object, or null if country or currency not found
     */
    fun getCurrencyObjectForCountry(countryCode: String): Currency? {
        val currencyCode = getCurrencyForCountry(countryCode) ?: return null
        return SupportedCurrencies.getByCode(currencyCode)
    }

    /**
     * Get all countries that use a specific currency.
     *
     * @param currencyCode ISO 4217 currency code (e.g., "USD", "EUR")
     * @return List of country codes that use this currency
     */
    fun getCountriesForCurrency(currencyCode: String): List<String> {
        return countryToCurrency.entries
            .filter { it.value.equals(currencyCode, ignoreCase = true) }
            .map { it.key }
    }

    /**
     * Check if a country code has a currency mapping.
     *
     * @param countryCode ISO 3166-1 alpha-2 country code
     * @return true if the country has a currency mapping, false otherwise
     */
    fun hasMapping(countryCode: String): Boolean {
        return countryToCurrency.containsKey(countryCode.uppercase())
    }

    /**
     * Get all available country-to-currency mappings.
     *
     * @return Map of country codes to currency codes
     */
    fun getAllMappings(): Map<String, String> = countryToCurrency.toMap()
}
