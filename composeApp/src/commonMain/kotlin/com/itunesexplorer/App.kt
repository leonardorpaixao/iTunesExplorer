package com.itunesexplorer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.itunesexplorer.design.theme.ITunesExplorerTheme
import com.itunesexplorer.di.appDI
import cafe.adriel.lyricist.LocalStrings
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.itunesexplorer.i18n.ProvideFeatureStrings
import com.itunesexplorer.i18n.getSystemLanguage
import com.itunesexplorer.home.presentation.HomeScreen
import com.itunesexplorer.settings.data.PreferencesRepository
import com.itunesexplorer.settings.language.LanguageManager
import org.kodein.di.compose.withDI
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun App() {
    withDI(appDI) {
        val di = localDI()
        val preferencesRepository: PreferencesRepository by di.instance()

        // Initialize language on first composition
        LaunchedEffect(Unit) {
            // Priority: Saved preference > System language > English
            val savedLanguage = preferencesRepository.getLanguage()
            val systemLanguage = getSystemLanguage()
            val initialLanguage = savedLanguage ?: systemLanguage

            println("🌍 [App] System language detected: $systemLanguage")
            println("💾 [App] Saved language: $savedLanguage")
            println("✅ [App] Initial language: $initialLanguage")

            LanguageManager.initialize(initialLanguage)
        }

        // Observe language changes from LanguageManager
        val currentLanguage by LanguageManager.currentLanguage.collectAsState()

        println("🔄 [App] Current language from LanguageManager: $currentLanguage")

        // Only render when language is initialized
        currentLanguage?.let { language ->
            println("🎨 [App] Rendering with language: $language")

            setSingletonImageLoaderFactory { context ->
                ImageLoader.Builder(context)
                    .crossfade(true)
                    .logger(DebugLogger())
                    .memoryCache {
                        MemoryCache.Builder()
                            .maxSizePercent(context, 0.25)
                            .build()
                    }
                    .build()
            }

            // Use key() to force complete recomposition when language changes
            key(language) {
                println("🔑 [App] Key recomposition triggered for language: $language")

                // Create Lyricist with current language
                val lyricist = rememberStrings(currentLanguageTag = language)
                println("📚 [App] Lyricist created with tag: ${lyricist.languageTag}")

                ProvideStrings(lyricist = lyricist) {
                    ProvideFeatureStrings(appStrings = LocalStrings.current) {
                        ITunesExplorerTheme {
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
