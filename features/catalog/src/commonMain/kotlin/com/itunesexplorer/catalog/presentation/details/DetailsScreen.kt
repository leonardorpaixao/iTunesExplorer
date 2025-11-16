package com.itunesexplorer.catalog.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalCatalogStrings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.itunesexplorer.common.extensions.toFormattedPrice
import com.itunesexplorer.design.components.ErrorMessage
import com.itunesexplorer.design.components.MediaCard
import com.itunesexplorer.network.models.ITunesItem
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance

data class DetailsScreen(
    val itemId: String,
    override val di: DI
) : Screen, DIAware {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: DetailsScreenModel by instance(arg = itemId)
        val state by screenModel.state.collectAsState()
        val strings = LocalCatalogStrings.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(strings.details) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Text("←")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                DetailsContent(
                    state = state,
                    onAction = screenModel::onAction,
                    strings = strings
                )
            }
        }
    }
}

@Composable
fun DetailsContent(
    state: DetailsViewState,
    onAction: (DetailsIntent) -> Unit,
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
                onRetry = { onAction(DetailsIntent.Retry) },
                retryText = strings.retry
            )
        }
        state.item == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = strings.noDetails,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ItemDetailsCard(item = state.item, onAction = onAction, strings = strings)
                }

                if (state.relatedItems.isNotEmpty()) {
                    item {
                        Text(
                            text = strings.relatedItems,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(state.relatedItems) { relatedItem ->
                        MediaCard(
                            title = relatedItem.trackName ?: relatedItem.collectionName ?: "Unknown",
                            subtitle = relatedItem.artistName ?: "Unknown Artist",
                            imageUrl = relatedItem.artworkUrl100,
                            price = relatedItem.trackPrice?.toFormattedPrice()
                                ?: relatedItem.collectionPrice?.toFormattedPrice(),
                            onClick = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemDetailsCard(
    item: ITunesItem,
    onAction: (DetailsIntent) -> Unit,
    strings: com.itunesexplorer.catalog.i18n.CatalogStrings
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Artwork
            item.artworkUrl100?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl.replace("100x100", "600x600"),
                    contentDescription = item.trackName ?: item.collectionName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Title
            Text(
                text = item.trackName ?: item.collectionName ?: "Unknown",
                style = MaterialTheme.typography.headlineSmall
            )

            // Artist
            item.artistName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Genre
            item.primaryGenreName?.let {
                Text(
                    text = "${strings.genre}: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Price
            val price = item.trackPrice?.toFormattedPrice()
                ?: item.collectionPrice?.toFormattedPrice()
            price?.let {
                Text(
                    text = "${strings.price}: $it",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Release Date
            item.releaseDate?.let {
                Text(
                    text = "${strings.releaseDate}: ${it.substringBefore("T")}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Track Count
            item.trackCount?.let {
                Text(
                    text = "${strings.trackCount}: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Description
            item.longDescription?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } ?: item.shortDescription?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Open in Store Button
            Button(
                onClick = { onAction(DetailsIntent.OpenInStore) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(strings.openInStore)
            }
        }
    }
}
