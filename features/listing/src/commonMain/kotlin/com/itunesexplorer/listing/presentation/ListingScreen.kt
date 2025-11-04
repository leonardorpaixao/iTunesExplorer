package com.itunesexplorer.listing.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.itunesexplorer.common.extensions.toFormattedPrice
import com.itunesexplorer.design.components.*
import com.itunesexplorer.network.models.ITunesItem
import com.itunesexplorer.network.models.MediaType

class ListingScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ListingScreenModel>()
        val state by screenModel.state.collectAsState()

        ListingContent(
            state = state,
            onSearchQueryChange = screenModel::updateSearchQuery,
            onSearch = { screenModel.search() },
            onMediaTypeSelected = screenModel::selectMediaType,
            onItemClick = { item ->
                // TODO: Implement item detail view
            },
            onRetry = screenModel::retry
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingContent(
    state: ListingState,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onMediaTypeSelected: (MediaType) -> Unit,
    onItemClick: (ITunesItem) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("iTunes Explorer") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onSearch = onSearch,
                    placeholder = "Search music, movies, podcasts..."
                )
            }
            
            // Media Type Filter
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(MediaType.values()) { mediaType ->
                    FilterChip(
                        selected = state.selectedMediaType == mediaType,
                        onClick = { onMediaTypeSelected(mediaType) },
                        label = { 
                            Text(
                                mediaType.name.replace("_", " ")
                                    .lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            )
                        }
                    )
                }
            }
            
            // Content
            when {
                state.isLoading -> {
                    LoadingIndicator()
                }
                state.error != null -> {
                    ErrorMessage(
                        message = state.error,
                        onRetry = onRetry
                    )
                }
                state.items.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (state.searchQuery.isEmpty()) 
                                "Search for your favorite content" 
                            else 
                                "No results found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.items) { item ->
                            MediaCard(
                                title = item.trackName ?: item.collectionName ?: "Unknown",
                                subtitle = item.artistName ?: "Unknown Artist",
                                imageUrl = item.artworkUrl100,
                                price = item.trackPrice?.toFormattedPrice() 
                                    ?: item.collectionPrice?.toFormattedPrice(),
                                onClick = { onItemClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}
