package com.itunesexplorer.catalog.presentation.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.presentation.formatReleaseDate
import com.itunesexplorer.catalog.presentation.i18n.CatalogStrings

@Composable
internal fun ItemDetailsCard(
    item: SearchResult,
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
                        model = imageUrl.replace(
                            CatalogConstants.THUMBNAIL_SIZE,
                            CatalogConstants.FULL_SIZE
                        ),
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