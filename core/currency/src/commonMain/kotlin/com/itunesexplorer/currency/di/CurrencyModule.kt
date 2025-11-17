package com.itunesexplorer.currency.di

import org.kodein.di.DI

/**
 * Kodein DI module for currency-related dependencies.
 *
 * Note: CurrencyFormatter, CurrencyMapper, and SupportedCurrencies are all
 * implemented as objects (singletons), so no explicit bindings are needed.
 * This module is provided for consistency and future extensibility.
 */
val currencyModule = DI.Module("currencyModule") {
    // No bindings needed at this time as all currency utilities are objects
    // This module is here for future extensibility
}
