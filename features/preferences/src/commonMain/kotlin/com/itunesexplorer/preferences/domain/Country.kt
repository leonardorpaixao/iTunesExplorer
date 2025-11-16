package com.itunesexplorer.preferences.domain

/**
 * Represents a country/region option for iTunes Search API
 * @param code ISO 3166-1 alpha-2 country code (e.g., "US", "BR")
 * @param nativeName Native name of the country in English (for reference)
 */
data class Country(
    val code: String,
    val nativeName: String
)

/**
 * List of supported countries for iTunes Search API
 * Organized by region for better UX
 */
object SupportedCountries {

    // Special option
    val NONE = Country("", "None")

    // Americas
    val UNITED_STATES = Country("US", "United States")
    val BRAZIL = Country("BR", "Brazil")
    val CANADA = Country("CA", "Canada")
    val MEXICO = Country("MX", "Mexico")
    val ARGENTINA = Country("AR", "Argentina")
    val CHILE = Country("CL", "Chile")
    val COLOMBIA = Country("CO", "Colombia")

    // Europe
    val UNITED_KINGDOM = Country("GB", "United Kingdom")
    val GERMANY = Country("DE", "Germany")
    val FRANCE = Country("FR", "France")
    val SPAIN = Country("ES", "Spain")
    val ITALY = Country("IT", "Italy")
    val PORTUGAL = Country("PT", "Portugal")
    val NETHERLANDS = Country("NL", "Netherlands")
    val BELGIUM = Country("BE", "Belgium")
    val SWITZERLAND = Country("CH", "Switzerland")
    val AUSTRIA = Country("AT", "Austria")
    val SWEDEN = Country("SE", "Sweden")
    val NORWAY = Country("NO", "Norway")
    val DENMARK = Country("DK", "Denmark")
    val FINLAND = Country("FI", "Finland")
    val POLAND = Country("PL", "Poland")
    val RUSSIA = Country("RU", "Russia")

    // Asia-Pacific
    val JAPAN = Country("JP", "Japan")
    val AUSTRALIA = Country("AU", "Australia")
    val NEW_ZEALAND = Country("NZ", "New Zealand")
    val SOUTH_KOREA = Country("KR", "South Korea")
    val CHINA = Country("CN", "China")
    val INDIA = Country("IN", "India")
    val SINGAPORE = Country("SG", "Singapore")
    val THAILAND = Country("TH", "Thailand")

    // Middle East & Africa
    val UNITED_ARAB_EMIRATES = Country("AE", "United Arab Emirates")
    val SOUTH_AFRICA = Country("ZA", "South Africa")

    /**
     * All available countries sorted alphabetically by code for consistency
     * NONE is placed first as a special option
     */
    val all: List<Country> = listOf(
        NONE,                 // "" (None)
        UNITED_ARAB_EMIRATES, // AE
        ARGENTINA,            // AR
        AUSTRIA,              // AT
        AUSTRALIA,            // AU
        BELGIUM,              // BE
        BRAZIL,               // BR
        CANADA,               // CA
        SWITZERLAND,          // CH
        CHILE,                // CL
        CHINA,                // CN
        COLOMBIA,             // CO
        GERMANY,              // DE
        DENMARK,              // DK
        SPAIN,                // ES
        FINLAND,              // FI
        FRANCE,               // FR
        UNITED_KINGDOM,       // GB
        INDIA,                // IN
        ITALY,                // IT
        JAPAN,                // JP
        SOUTH_KOREA,          // KR
        MEXICO,               // MX
        NETHERLANDS,          // NL
        NORWAY,               // NO
        NEW_ZEALAND,          // NZ
        POLAND,               // PL
        PORTUGAL,             // PT
        RUSSIA,               // RU
        SWEDEN,               // SE
        SINGAPORE,            // SG
        THAILAND,             // TH
        UNITED_STATES,        // US
        SOUTH_AFRICA,         // ZA
    )

    /**
     * Gets a country by its code
     * @param code ISO 3166-1 alpha-2 country code
     * @return Country if found, null otherwise
     */
    fun getByCode(code: String): Country? {
        return all.find { it.code.equals(code, ignoreCase = true) }
    }
}
