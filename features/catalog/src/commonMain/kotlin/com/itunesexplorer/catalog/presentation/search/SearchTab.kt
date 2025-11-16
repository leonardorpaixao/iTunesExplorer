package com.itunesexplorer.catalog.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalCatalogStrings
import com.itunesexplorer.common.extensions.toFormattedPrice
import com.itunesexplorer.design.components.ErrorMessage
import com.itunesexplorer.design.components.LoadingIndicator
import com.itunesexplorer.design.components.MediaCard
import com.itunesexplorer.network.models.MediaType
import org.kodein.di.compose.rememberInstance

@Composable
fun SearchTab(
    onItemClick: (String) -> Unit = {}
) {
    val screenModel: SearchTabModel by rememberInstance()
    val state by screenModel.state.collectAsState()
    val strings = LocalCatalogStrings.current

    SearchTabContent(
        state = state,
        onAction = screenModel::onAction,
        onItemClick = onItemClick,
        strings = strings
    )
}

@Composable
fun SearchTabContent(
    state: SearchViewState,
    onAction: (SearchIntent) -> Unit,
    onItemClick: (String) -> Unit,
    strings: com.itunesexplorer.catalog.i18n.CatalogStrings
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            com.itunesexplorer.design.components.SearchBar(
                query = state.searchQuery,
                onQueryChange = { onAction(SearchIntent.UpdateSearchQuery(it)) },
                onSearch = { onAction(SearchIntent.Search) },
                placeholder = strings.searchPlaceholder,
                searchIconContentDescription = strings.search
            )
        }

        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(MediaType.values()) { mediaType ->
                FilterChip(
                    selected = state.selectedMediaType == mediaType,
                    onClick = { onAction(SearchIntent.SelectMediaType(mediaType)) },
                    label = {
                        Text(strings.mediaTypeChip(mediaType))
                    }
                )
            }
        }

        when {
            state.isLoading -> {
                LoadingIndicator()
            }
            state.error != null -> {
                ErrorMessage(
                    message = state.error,
                    onRetry = { onAction(SearchIntent.Retry) },
                    retryText = strings.retry
                )
            }
            state.items.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = strings.noResults,
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
                        val itemId = item.trackId?.toString()
                            ?: item.collectionId?.toString()
                            ?: ""
                        MediaCard(
                            title = item.trackName ?: item.collectionName ?: "Unknown",
                            subtitle = item.artistName ?: "Unknown Artist",
                            imageUrl = item.artworkUrl100,
                            price = item.trackPrice?.toFormattedPrice()
                                ?: item.collectionPrice?.toFormattedPrice(),
                            onClick = { onItemClick(itemId) }
                        )
                    }
                }
            }
        }
    }
}
