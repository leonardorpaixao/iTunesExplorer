package com.itunesexplorer

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.itunesexplorer.design.theme.ITunesExplorerTheme
import com.itunesexplorer.listing.presentation.ListingScreen
import com.itunesexplorer.di.appDI
import org.kodein.di.compose.withDI

@Composable
fun App() {
    withDI(appDI) {
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
