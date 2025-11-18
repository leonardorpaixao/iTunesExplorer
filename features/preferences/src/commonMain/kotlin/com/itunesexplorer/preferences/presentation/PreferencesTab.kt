package com.itunesexplorer.preferences.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalPreferencesStrings
import com.itunesexplorer.design.components.ErrorContent
import com.itunesexplorer.preferences.domain.Language
import com.itunesexplorer.preferences.i18n.PreferencesStrings
import org.kodein.di.compose.rememberInstance

@Composable
fun PreferencesTab() {
    val screenModel: PreferencesTabViewModel by rememberInstance()
    val state by screenModel.state.collectAsState()
    val strings = LocalPreferencesStrings.current

    PreferencesEffectHandler(
        effectFlow = screenModel.effect,
        onAction = screenModel::onAction,
        strings = strings
    )

    PreferencesTabContent(
        state = state,
        onAction = screenModel::onAction,
        strings = strings
    )
}

@Composable
fun PreferencesTabContent(
    state: PreferencesViewState,
    onAction: (PreferencesIntent) -> Unit,
    strings: PreferencesStrings
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = strings.preferences,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        state.error?.let { error ->
            ErrorContent(error)
        }

        Text(
            text = strings.language,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    LazyColumn {
                        items(state.availableLanguages) { language ->
                            LanguageItem(
                                language = language,
                                isSelected = language.code == state.selectedLanguage,
                                onClick = { onAction(PreferencesIntent.SelectLanguage(language.code)) }
                            )
                            if (language != state.availableLanguages.last()) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = strings.country,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = strings.countryHelpText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onAction(PreferencesIntent.OpenCountrySelectionModal(state.selectedCountry))
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strings.currentCountry,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = strings.countryName(state.selectedCountry),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = language.nativeName,
            style = MaterialTheme.typography.bodyLarge
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun LanguageChangeDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    strings: PreferencesStrings
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = strings.confirmLanguageChange)
        },
        text = {
            Text(text = strings.confirmLanguageMessage)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(strings.confirm)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}

