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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.domain.model.MusicGenre
import com.itunesexplorer.catalog.presentation.details.DetailsScreen
import com.itunesexplorer.catalog.presentation.i18n.CatalogStrings
import com.itunesexplorer.design.components.ErrorContent
import com.itunesexplorer.design.components.MediaCard
import org.kodein.di.compose.localDI
import org.kodein.di.compose.rememberInstance

@Composable
fun AlbumsTab() {
    val screenModel: AlbumsTabViewModel by rememberInstance()
    val state by screenModel.state.collectAsState()
    val strings = LocalCatalogStrings.current
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        screenModel.effect.collect { effect ->
            when (effect) {
                is AlbumsEffect.NavigateToDetails -> {
                    navigator.push(DetailsScreen(effect.itemId))
                }
            }
        }
    }

    AlbumsTabContent(
        state = state,
        onAction = screenModel::dispatch,
        strings = strings
    )
}

@Composable
internal fun AlbumsTabContent(
    state: AlbumsViewState,
    onAction: (AlbumsIntent) -> Unit,
    strings: CatalogStrings
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
            ErrorContent(
                error = state.error,
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
                        onClick = { onAction(AlbumsIntent.ItemClicked(album.id)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(
    album: Album,
    onClick: () -> Unit
) {
    MediaCard(
        title = album.name,
        subtitle = "${album.artistName} • ${album.genre}",
        imageUrl = album.imageUrl,
        price = album.price?.format(),
        onClick = onClick
    )
}

@Composable
fun GenreChipsRow(
    selectedGenre: MusicGenre,
    onGenreSelected: (MusicGenre) -> Unit,
    strings: CatalogStrings,
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
