package com.itunesexplorer.settings.country

import com.itunesexplorer.settings.CountryManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for CountryManager singleton.
 * Note: Since CountryManager is a singleton, state persists between tests.
 * Tests are designed to be independent of initial state.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CountryManagerTest {

    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `setCountry should update country value`() = runTest(testDispatcher) {
        // Given - Set initial state
        CountryManager.setCountry("US")
        advanceUntilIdle()

        // When
        CountryManager.setCountry("BR")
        advanceUntilIdle()

        // Then
        val result = CountryManager.currentCountry.first()
        assertEquals("BR", result)
    }

    @Test
    fun `getCurrentCountryCode should return current country`() = runTest(testDispatcher) {
        // Given
        CountryManager.setCountry("FR")
        advanceUntilIdle()

        // When
        val result = CountryManager.getCurrentCountryCode()

        // Then
        assertEquals("FR", result)
    }

    @Test
    fun `setCountry should emit new value immediately`() = runTest(testDispatcher) {
        // Given
        val firstCountry = "GB"
        val secondCountry = "JP"

        // When - Set first country
        CountryManager.setCountry(firstCountry)
        advanceUntilIdle()

        val afterFirst = CountryManager.currentCountry.first()
        assertEquals(firstCountry, afterFirst)

        // When - Set second country
        CountryManager.setCountry(secondCountry)
        advanceUntilIdle()

        // Then
        val afterSecond = CountryManager.currentCountry.first()
        assertEquals(secondCountry, afterSecond)
    }

    @Test
    fun `multiple setCountry calls should update to latest value`() = runTest(testDispatcher) {
        // Given & When - Multiple updates
        CountryManager.setCountry("US")
        CountryManager.setCountry("BR")
        CountryManager.setCountry("DE")
        advanceUntilIdle()

        // Then - Should have latest value
        val result = CountryManager.getCurrentCountryCode()
        assertEquals("DE", result)
    }

    @Test
    fun `initialize should set country if called when not null`() = runTest(testDispatcher) {
        // Given - Set a country first
        CountryManager.setCountry("US")
        advanceUntilIdle()

        // When - Try to initialize with different country
        CountryManager.initialize("BR")
        advanceUntilIdle()

        // Then - Should still be US (initialize doesn't override)
        val result = CountryManager.getCurrentCountryCode()
        assertEquals("US", result)
    }

    @Test
    fun `country changes should be observable through StateFlow`() = runTest(testDispatcher) {
        // Given
        val testCountries = listOf("AU", "NZ", "SG")

        // When & Then - Each change should be observable
        testCountries.forEach { country ->
            CountryManager.setCountry(country)
            advanceUntilIdle()

            val current = CountryManager.currentCountry.first()
            assertEquals(country, current)
        }
    }
}
