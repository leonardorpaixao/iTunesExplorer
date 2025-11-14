package com.itunesexplorer.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.lyricist.LocalListingStrings

/**
 * Centralized provider for all feature module i18n strings.
 *
 * When adding a new feature module with strings:
 * 1. Add the strings field to AppStrings data class
 * 2. Add the CompositionLocal provider here
 *
 * This keeps App.kt clean and scalable.
 */
@Composable
fun ProvideFeatureStrings(
    appStrings: AppStrings,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalListingStrings provides appStrings.listing,
        // Add new feature strings here as the app grows:
        // LocalDetailsStrings provides appStrings.details,
        // LocalProfileStrings provides appStrings.profile,
    ) {
        content()
    }
}
