package com.itunesexplorer.catalog.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalCatalogStrings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.itunesexplorer.catalog.presentation.details.components.ItemDetailsCard
import com.itunesexplorer.catalog.presentation.i18n.CatalogStrings
import com.itunesexplorer.design.components.ErrorContent
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

        DetailsEffectHandler(screenModel.effect)

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
                    onAction = screenModel::dispatch,
                    strings = strings
                )
            }
        }
    }
}

@Composable
internal fun DetailsContent(
    state: DetailsViewState,
    onAction: (DetailsIntent) -> Unit,
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
                        ItemDetailsCard(item = state.item, strings = strings)
                    }
                }

                Button(
                    onClick = { onAction(DetailsIntent.Back) },
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


