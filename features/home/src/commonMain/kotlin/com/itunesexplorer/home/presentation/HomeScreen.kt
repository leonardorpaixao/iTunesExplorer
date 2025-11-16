package com.itunesexplorer.home.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalHomeStrings
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.itunesexplorer.design.components.BottomNavItem
import com.itunesexplorer.design.components.BottomNavigationBar
import com.itunesexplorer.design.icons.rememberAlbumIcon
import com.itunesexplorer.design.icons.rememberPlayCircleIcon
import com.itunesexplorer.design.icons.rememberSearchIcon
import com.itunesexplorer.design.icons.rememberSettingsIcon
import com.itunesexplorer.home.presentation.albums.AlbumsTab
import com.itunesexplorer.home.presentation.search.SearchTab
import com.itunesexplorer.home.presentation.preferences.PreferencesTab

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeScreenModel>()
        val state by screenModel.state.collectAsState()
        val strings = LocalHomeStrings.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .clickable { screenModel.onAction(HomeIntent.SelectTab(HomeTab.ALBUMS)) }
                                .padding(vertical = 8.dp)
                                .padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = strings.appName)
                            Icon(
                                imageVector = rememberPlayCircleIcon(),
                                contentDescription = strings.appName,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(28.dp)
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    items = listOf(
                        BottomNavItem(
                            label = strings.tabAlbums,
                            icon = { Icon(rememberAlbumIcon(), contentDescription = strings.tabAlbums) },
                            selected = state.selectedTab == HomeTab.ALBUMS,
                            onClick = { screenModel.onAction(HomeIntent.SelectTab(HomeTab.ALBUMS)) }
                        ),
                        BottomNavItem(
                            label = strings.tabSearch,
                            icon = { Icon(rememberSearchIcon(), contentDescription = strings.tabSearch) },
                            selected = state.selectedTab == HomeTab.SEARCH,
                            onClick = { screenModel.onAction(HomeIntent.SelectTab(HomeTab.SEARCH)) }
                        ),
                        BottomNavItem(
                            label = strings.tabPreferences,
                            icon = { Icon(rememberSettingsIcon(), contentDescription = strings.tabPreferences) },
                            selected = state.selectedTab == HomeTab.PREFERENCES,
                            onClick = { screenModel.onAction(HomeIntent.SelectTab(HomeTab.PREFERENCES)) }
                        )
                    )
                )
            }
        ) { paddingValues ->
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                when (state.selectedTab) {
                    HomeTab.ALBUMS -> AlbumsTab()
                    HomeTab.SEARCH -> SearchTab()
                    HomeTab.PREFERENCES -> PreferencesTab()
                }
            }
        }
    }
}
