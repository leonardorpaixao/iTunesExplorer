package com.itunesexplorer.catalog.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalCatalogStrings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.presentation.format
import com.itunesexplorer.catalog.presentation.formatReleaseDate
import com.itunesexplorer.catalog.presentation.i18n.CatalogStrings
import com.itunesexplorer.design.components.ErrorMessage
import com.itunesexplorer.design.components.MediaCard
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
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = strings.back
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                DetailsContent(
                    state = state,
                    onAction = screenModel::onAction,
                    onBack = { navigator.pop() },
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
    onBack: () -> Unit,
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
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
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
                                title = relatedItem.name,
                                subtitle = relatedItem.artistName ?: CatalogConstants.UNKNOWN_ARTIST,
                                imageUrl = relatedItem.imageUrl,
                                price = relatedItem.price?.format(),
                                onClick = { }
                            )
                        }
                    }
                }

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(strings.back)
                }
            }
        }
    }
}

@Composable
fun ItemDetailsCard(
    item: SearchResult,
    onAction: (DetailsIntent) -> Unit,
    strings: CatalogStrings
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item.imageUrl?.let { imageUrl ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl.replace(CatalogConstants.THUMBNAIL_SIZE, CatalogConstants.FULL_SIZE),
                        contentDescription = item.name,
                        modifier = Modifier
                            .widthIn(max = 400.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineSmall
            )

            item.artistName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item.genre?.let {
                Text(
                    text = "${strings.genre}: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item.price?.let { money ->
                Text(
                    text = "${strings.price}: ${money.format()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item.releaseDate?.let {
                Text(
                    text = "${strings.releaseDate}: ${it.formatReleaseDate(strings)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
