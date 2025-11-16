package com.itunesexplorer.preferences.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalPreferencesStrings
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.itunesexplorer.preferences.domain.Country
import com.itunesexplorer.preferences.domain.SupportedCountries
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.data.PreferencesRepository
import kotlinx.coroutines.launch
import org.kodein.di.compose.rememberInstance

data class CountrySelectionScreen(
    val selectedCountry: String
) : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val preferencesRepository: PreferencesRepository by rememberInstance()
        val coroutineScope = rememberCoroutineScope()
        val strings = LocalPreferencesStrings.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(2.dp)
                    )
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { bottomSheetNavigator.hide() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = strings.cancel
                    )
                }

                Text(
                    text = strings.chooseCountry,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(SupportedCountries.all) { country ->
                    CountryItem(
                        country = country,
                        isSelected = country.code == selectedCountry,
                        onClick = {
                            coroutineScope.launch {
                                // If empty code (None option), clear country preference
                                if (country.code.isEmpty()) {
                                    preferencesRepository.clearCountry()
                                    CountryManager.clear()
                                } else {
                                    preferencesRepository.setCountry(country.code)
                                    CountryManager.setCountry(country.code)
                                }
                            }
                            bottomSheetNavigator.hide()
                        },
                        countryName = strings.countryName(country.code)
                    )
                    if (country != SupportedCountries.all.last()) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun CountryItem(
    country: Country,
    isSelected: Boolean,
    onClick: () -> Unit,
    countryName: String,
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
            text = countryName,
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
