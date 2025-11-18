package com.itunesexplorer.home.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import com.itunesexplorer.catalog.presentation.albums.AlbumsTab
import com.itunesexplorer.catalog.presentation.search.SearchTab
import com.itunesexplorer.preferences.presentation.PreferencesTab
import org.kodein.di.DI
import org.kodein.di.DIAware

class HomeScreen(override val di: DI) : Screen, DIAware {

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
                                .clickable { screenModel.dispatch(HomeIntent.SelectTab(HomeTab.ALBUMS)) }
                                .padding(vertical = 8.dp)
                                .padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = strings.appName)
                            Icon(
                                imageVector = Icons.Filled.PlayCircle,
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
                            icon = { Icon(Icons.Filled.Album, contentDescription = strings.tabAlbums) },
                            selected = state.selectedTab == HomeTab.ALBUMS,
                            onClick = { screenModel.dispatch(HomeIntent.SelectTab(HomeTab.ALBUMS)) }
                        ),
                        BottomNavItem(
                            label = strings.tabSearch,
                            icon = { Icon(Icons.Filled.Search, contentDescription = strings.tabSearch) },
                            selected = state.selectedTab == HomeTab.SEARCH,
                            onClick = { screenModel.dispatch(HomeIntent.SelectTab(HomeTab.SEARCH)) }
                        ),
                        BottomNavItem(
                            label = strings.tabPreferences,
                            icon = { Icon(Icons.Filled.Settings, contentDescription = strings.tabPreferences) },
                            selected = state.selectedTab == HomeTab.PREFERENCES,
                            onClick = { screenModel.dispatch(HomeIntent.SelectTab(HomeTab.PREFERENCES)) }
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
