package com.itunesexplorer.catalog.presentation.albums

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
import com.itunesexplorer.design.components.ErrorMessage
import com.itunesexplorer.design.components.MediaCard
import com.itunesexplorer.catalog.shared.data.models.RssFeedEntry
import com.itunesexplorer.network.models.MusicGenre
import org.kodein.di.compose.rememberInstance

@Composable
fun AlbumsTab(
    onItemClick: (String) -> Unit = {}
) {
    val screenModel: AlbumsTabModel by rememberInstance()
    val state by screenModel.state.collectAsState()
    val strings = LocalCatalogStrings.current

    AlbumsTabContent(
        state = state,
        onAction = screenModel::onAction,
        onItemClick = onItemClick,
        strings = strings
    )
}

@Composable
fun AlbumsTabContent(
    state: AlbumsViewState,
    onAction: (AlbumsIntent) -> Unit,
    onItemClick: (String) -> Unit,
    strings: com.itunesexplorer.catalog.i18n.CatalogStrings
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

                item {
                    GenreChipsRow(
                        selectedGenre = state.selectedGenre,
                        onGenreSelected = { genre ->
                            onAction(AlbumsIntent.SelectGenre(genre))
                        },
                        strings = strings
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(state.recommendations) { album ->
                    RecommendationCard(
                        album = album,
                        onClick = { onItemClick(album.id.attributes.imId) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(
    album: RssFeedEntry,
    onClick: () -> Unit
) {
    val imageUrl = album.imImage.lastOrNull()?.label ?: ""
    val artistName = album.imArtist?.label ?: "Unknown Artist"
    val price = album.imPrice?.label

    MediaCard(
        title = album.imName.label,
        subtitle = "$artistName • ${album.category.attributes.label}",
        imageUrl = imageUrl,
        price = price,
        onClick = onClick
    )
}

@Composable
fun GenreChipsRow(
    selectedGenre: MusicGenre,
    onGenreSelected: (MusicGenre) -> Unit,
    strings: com.itunesexplorer.catalog.i18n.CatalogStrings,
    modifier: Modifier = Modifier
) {
    val genres = listOf(
        MusicGenre.ALL,
        MusicGenre.ROCK,
        MusicGenre.POP,
        MusicGenre.JAZZ,
        MusicGenre.BLUES,
        MusicGenre.CLASSICAL,
        MusicGenre.HIP_HOP_RAP,
        MusicGenre.ELECTRONIC,
        MusicGenre.COUNTRY,
        MusicGenre.R_B_SOUL,
        MusicGenre.ALTERNATIVE,
        MusicGenre.METAL,
        MusicGenre.INDIE
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(genres) { genre ->
            FilterChip(
                selected = selectedGenre == genre,
                onClick = { onGenreSelected(genre) },
                label = {
                    Text(text = strings.musicGenreChip(genre))
                }
            )
        }
    }
}
