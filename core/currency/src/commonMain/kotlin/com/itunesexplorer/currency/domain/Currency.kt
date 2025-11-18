package com.itunesexplorer.currency.domain

/**
 * Represents a currency with its ISO 4217 code, symbol, name, and decimal precision.
 *
 * @property code ISO 4217 3-letter currency code (e.g., "USD", "EUR")
 * @property symbol Currency symbol (e.g., "$", "€", "¥")
 * @property name Full currency name (e.g., "US Dollar", "Euro")
 * @property decimals Number of decimal places for this currency (e.g., 2 for USD, 0 for JPY)
 */
data class Currency(
    val code: String,
    val symbol: String,
    val name: String,
    val decimals: Int = 2
)

/**
 * Collection of all supported currencies in the iTunes Search API.
 * Based on research of 40+ countries using the iTunes API.
 */
object SupportedCurrencies {
    // Major Currencies
    val USD = Currency("USD", "$", "US Dollar", 2)
    val EUR = Currency("EUR", "€", "Euro", 2)
    val GBP = Currency("GBP", "£", "British Pound", 2)
    val JPY = Currency("JPY", "¥", "Japanese Yen", 0)
    val CNY = Currency("CNY", "¥", "Chinese Yuan", 2)

    // Americas
    val CAD = Currency("CAD", "CA$", "Canadian Dollar", 2)
    val BRL = Currency("BRL", "R$", "Brazilian Real", 2)
    val MXN = Currency("MXN", "MX$", "Mexican Peso", 2)
    val ARS = Currency("ARS", "AR$", "Argentine Peso", 2)
    val CLP = Currency("CLP", "CL$", "Chilean Peso", 0)
    val COP = Currency("COP", "CO$", "Colombian Peso", 0)
    val PEN = Currency("PEN", "S/", "Peruvian Sol", 2)

    // Asia-Pacific
    val AUD = Currency("AUD", "AU$", "Australian Dollar", 2)
    val NZD = Currency("NZD", "NZ$", "New Zealand Dollar", 2)
    val INR = Currency("INR", "₹", "Indian Rupee", 2)
    val KRW = Currency("KRW", "₩", "South Korean Won", 0)
    val SGD = Currency("SGD", "S$", "Singapore Dollar", 2)
    val HKD = Currency("HKD", "HK$", "Hong Kong Dollar", 2)
    val TWD = Currency("TWD", "NT$", "Taiwan Dollar", 2)
    val THB = Currency("THB", "฿", "Thai Baht", 2)
    val MYR = Currency("MYR", "RM", "Malaysian Ringgit", 2)
    val IDR = Currency("IDR", "Rp", "Indonesian Rupiah", 0)
    val PHP = Currency("PHP", "₱", "Philippine Peso", 2)
    val VND = Currency("VND", "₫", "Vietnamese Dong", 0)

    // Europe (Non-Euro)
    val CHF = Currency("CHF", "CHF", "Swiss Franc", 2)
    val SEK = Currency("SEK", "kr", "Swedish Krona", 2)
    val NOK = Currency("NOK", "kr", "Norwegian Krone", 2)
    val DKK = Currency("DKK", "kr", "Danish Krone", 2)
    val PLN = Currency("PLN", "zł", "Polish Zloty", 2)
    val CZK = Currency("CZK", "Kč", "Czech Koruna", 2)
    val HUF = Currency("HUF", "Ft", "Hungarian Forint", 0)
    val RON = Currency("RON", "lei", "Romanian Leu", 2)
    val TRY = Currency("TRY", "₺", "Turkish Lira", 2)
    val RUB = Currency("RUB", "₽", "Russian Ruble", 2)

    // Middle East & Africa
    val AED = Currency("AED", "AED", "UAE Dirham", 2)
    val SAR = Currency("SAR", "SR", "Saudi Riyal", 2)
    val ILS = Currency("ILS", "₪", "Israeli Shekel", 2)
    val ZAR = Currency("ZAR", "R", "South African Rand", 2)
    val EGP = Currency("EGP", "E£", "Egyptian Pound", 2)
    val NGN = Currency("NGN", "₦", "Nigerian Naira", 2)
    val KZT = Currency("KZT", "₸", "Kazakhstani Tenge", 2)

    /**
     * Map of currency codes to Currency objects for fast lookup.
     */
    private val codeMap: Map<String, Currency> = mapOf(
        USD.code to USD, EUR.code to EUR, GBP.code to GBP, JPY.code to JPY, CNY.code to CNY,
        CAD.code to CAD, BRL.code to BRL, MXN.code to MXN, ARS.code to ARS, CLP.code to CLP,
        COP.code to COP, PEN.code to PEN,
        AUD.code to AUD, NZD.code to NZD, INR.code to INR, KRW.code to KRW, SGD.code to SGD,
        HKD.code to HKD, TWD.code to TWD, THB.code to THB, MYR.code to MYR, IDR.code to IDR,
        PHP.code to PHP, VND.code to VND,
        CHF.code to CHF, SEK.code to SEK, NOK.code to NOK, DKK.code to DKK, PLN.code to PLN,
        CZK.code to CZK, HUF.code to HUF, RON.code to RON, TRY.code to TRY, RUB.code to RUB,
        AED.code to AED, SAR.code to SAR, ILS.code to ILS, ZAR.code to ZAR, EGP.code to EGP,
        NGN.code to NGN, KZT.code to KZT
    )

    /**
     * Get a currency by its ISO 4217 code.
     *
     * @param code The 3-letter currency code (e.g., "USD", "EUR")
     * @return The corresponding Currency object, or null if not found
     */
    fun getByCode(code: String): Currency? = codeMap[code.uppercase()]
}
