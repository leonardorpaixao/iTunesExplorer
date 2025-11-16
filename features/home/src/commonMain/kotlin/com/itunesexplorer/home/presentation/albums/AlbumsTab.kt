package com.itunesexplorer.home.presentation.albums

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalHomeStrings
import com.itunesexplorer.design.components.ErrorMessage
import com.itunesexplorer.design.components.MediaCard
import com.itunesexplorer.network.models.RssFeedEntry
import org.kodein.di.compose.rememberInstance

@Composable
fun AlbumsTab() {
    val screenModel: AlbumsTabModel by rememberInstance()
    val state by screenModel.state.collectAsState()
    val strings = LocalHomeStrings.current

    AlbumsTabContent(
        state = state,
        onAction = screenModel::onAction,
        strings = strings
    )
}

@Composable
fun AlbumsTabContent(
    state: AlbumsViewState,
    onAction: (AlbumsIntent) -> Unit,
    strings: com.itunesexplorer.home.i18n.HomeStrings
) {
    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            ErrorMessage(
                message = state.error,
                onRetry = { onAction(AlbumsIntent.Retry) },
                retryText = strings.retry
            )
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = strings.topAlbums,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = strings.topAlbumsDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(state.recommendations) { album ->
                    RecommendationCard(album = album)
                    Spacer(modifier = Modifier.height(8.dp))
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
        onClick = { }
    )
}
