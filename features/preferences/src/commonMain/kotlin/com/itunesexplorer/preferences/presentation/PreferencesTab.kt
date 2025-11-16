package com.itunesexplorer.preferences.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalPreferencesStrings
import com.itunesexplorer.preferences.domain.Language
import org.kodein.di.compose.rememberInstance

@Composable
fun PreferencesTab() {
    val screenModel: PreferencesTabModel by rememberInstance()
    val state by screenModel.state.collectAsState()
    val strings = LocalPreferencesStrings.current

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
    strings: com.itunesexplorer.preferences.i18n.PreferencesStrings
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

        // Language Section
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
    }

    // Confirmation Dialog
    if (state.showConfirmDialog) {
        LanguageChangeDialog(
            onConfirm = { onAction(PreferencesIntent.ConfirmLanguageChange) },
            onDismiss = { onAction(PreferencesIntent.DismissDialog) },
            strings = strings
        )
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
    strings: com.itunesexplorer.preferences.i18n.PreferencesStrings
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
