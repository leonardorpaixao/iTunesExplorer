package com.itunesexplorer.listing.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
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
import com.itunesexplorer.network.models.RssFeedEntry

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
            onRetry = screenModel::retry,
            onSurpriseMe = screenModel::surpriseMe
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
    onRetry: () -> Unit,
    onSurpriseMe: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .clickable { onSurpriseMe() }
                            .padding(vertical = 8.dp)
                            .padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "iTunes Explorer")
                        Icon(
                            imageVector = Icons.Filled.PlayCircle,
                            contentDescription = "Surprise Me",
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(28.dp)
                        )
                    }
                }
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
                state.items.isEmpty() && state.searchQuery.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No results found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                state.showRecommendations -> {
                    // Show recommendations when "Surprise Me" is clicked or on startup
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                                Text(
                                    text = "Top Álbuns",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = "The top trend albuns",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (state.isLoadingRecommendations) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        } else {
                            items(state.recommendations) { album ->
                                RecommendationCard(album = album)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                state.items.isEmpty() && state.searchQuery.isEmpty() && !state.showRecommendations -> {
                    // This state should rarely be reached since recommendations load on init
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
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

@Composable
fun RecommendationCard(album: RssFeedEntry) {
    val imageUrl = album.imImage.lastOrNull()?.label ?: ""
    val artistName = album.imArtist?.label ?: "Unknown Artist"
    val price = album.imPrice?.label

    MediaCard(
        title = album.imName.label,
        subtitle = "$artistName • ${album.category.attributes.label}",
        imageUrl = imageUrl,
        price = price,
        onClick = { /* TODO: Navigate to album details */ }
    )
}
