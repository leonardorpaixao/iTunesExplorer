package com.itunesexplorer

import androidx.compose.runtime.Composable
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
import org.kodein.di.compose.withDI
import org.kodein.di.compose.localDI

@Composable
fun App() {
    withDI(appDI) {
        val di = localDI()
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .logger(DebugLogger()) // TODO("Replace with system logger version")
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(context, 0.25)
                        .build()
                }
                .build()
        }
        val systemLanguage = getSystemLanguage()
        val lyricist = rememberStrings(currentLanguageTag = systemLanguage)

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
