package com.itunesexplorer.catalog.presentation.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.flow.Flow

@Composable
internal fun DetailsEffectHandler(
    effectFlow: Flow<DetailsEffect>,
) {
    val bottomSheetNavigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is DetailsEffect.Back -> {
                    bottomSheetNavigator?.pop()
                }
            }
        }
    }
}