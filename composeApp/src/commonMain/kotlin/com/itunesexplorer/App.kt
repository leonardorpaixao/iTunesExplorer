package com.itunesexplorer

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import com.itunesexplorer.design.theme.ITunesExplorerTheme
import com.itunesexplorer.di.appDI
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.itunesexplorer.i18n.ProvideFeatureStrings
import com.itunesexplorer.foundation.i18n.getSystemLanguage
import com.itunesexplorer.foundation.i18n.getSystemCountry
import com.itunesexplorer.home.presentation.HomeScreen
import com.itunesexplorer.settings.PreferencesRepository
import com.itunesexplorer.settings.LanguageManager
import com.itunesexplorer.settings.CountryManager
import org.kodein.di.compose.withDI
import org.kodein.di.compose.localDI
import org.kodein.di.instance

expect fun getAsyncImageLoader(context: PlatformContext): ImageLoader

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    withDI(appDI) {
        val di = localDI()
        val preferencesRepository: PreferencesRepository by di.instance()

        LaunchedEffect(Unit) {
            val savedLanguage = preferencesRepository.getLanguage()
            val systemLanguage = getSystemLanguage()
            val initialLanguage = savedLanguage ?: systemLanguage
            LanguageManager.initialize(initialLanguage)

            val savedCountry = preferencesRepository.getCountry()
            val systemCountry = getSystemCountry()
            val initialCountry = savedCountry ?: systemCountry
            CountryManager.initialize(initialCountry)
        }

        val currentLanguage by LanguageManager.currentLanguage.collectAsState()

        currentLanguage?.let { language ->
            setSingletonImageLoaderFactory { context ->
                getAsyncImageLoader(context)
            }

            key(language) {
                val lyricist = rememberStrings(currentLanguageTag = language)

                ProvideStrings(lyricist = lyricist) {
                    ProvideFeatureStrings(appStrings = LocalStrings.current) {
                        ITunesExplorerTheme {
                            BottomSheetNavigator(
                                hideOnBackPress = true,
                                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                                sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                                sheetContentColor = MaterialTheme.colorScheme.onSurface
                            ) {
                                Navigator(
                                    screen = HomeScreen(di),
                                    content = { navigator ->
                                        SlideTransition(navigator)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
