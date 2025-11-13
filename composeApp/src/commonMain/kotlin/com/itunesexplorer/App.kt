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
import com.itunesexplorer.listing.presentation.ListingScreen
import com.itunesexplorer.di.appDI
import org.kodein.di.compose.withDI

@Composable
fun App() {
    withDI(appDI) {
        setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .memoryCache {
                    MemoryCache.Builder()
                        .build()
                }
                .crossfade(true)
                .logger(DebugLogger()) // TODO("Replace with system logger version")
                .build()
        }

        ITunesExplorerTheme {
            Navigator(
                screen = ListingScreen(),
                content = { navigator ->
                    SlideTransition(navigator)
                }
            )
        }
    }
}
