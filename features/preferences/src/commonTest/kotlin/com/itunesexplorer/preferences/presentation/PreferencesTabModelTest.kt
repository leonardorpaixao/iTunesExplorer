package com.itunesexplorer.preferences.presentation

import app.cash.turbine.test
import com.itunesexplorer.preferences.domain.SupportedCountries
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.data.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesTabModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakePreferencesRepository: FakePreferencesRepository
    private lateinit var viewModel: PreferencesTabModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakePreferencesRepository = FakePreferencesRepository()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should load countries on init`() = runTest(testDispatcher) {
        // Given - Set initial country to avoid null
        CountryManager.setCountry("INIT")
        advanceUntilIdle()

        // When
        viewModel = PreferencesTabModel(fakePreferencesRepository)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(SupportedCountries.all.size, state.availableCountries.size)
        }
    }

    @Test
    fun `LoadCountries intent should populate availableCountries`() = runTest(testDispatcher) {
        // Given
        CountryManager.setCountry("INIT")
        advanceUntilIdle()

        viewModel = PreferencesTabModel(fakePreferencesRepository)
        advanceUntilIdle()

        // When
        viewModel.onAction(PreferencesIntent.LoadCountries)
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.availableCountries.isNotEmpty())
            assertEquals(SupportedCountries.all, state.availableCountries)
        }
    }

    @Test
    fun `should update selectedCountry when CountryManager changes`() = runTest(testDispatcher) {
        // Given - Set initial country
        CountryManager.setCountry("US")
        advanceUntilIdle()

        viewModel = PreferencesTabModel(fakePreferencesRepository)
        advanceUntilIdle()

        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals("US", initialState.selectedCountry)

            // When - Change country through CountryManager
            CountryManager.setCountry("BR")
            advanceUntilIdle()

            // Then - ViewModel state should be updated
            val updatedState = awaitItem()
            assertEquals("BR", updatedState.selectedCountry)
        }
    }

    @Test
    fun `should reactively update selectedCountry on multiple changes`() = runTest(testDispatcher) {
        // Given
        CountryManager.setCountry("GB")
        advanceUntilIdle()

        viewModel = PreferencesTabModel(fakePreferencesRepository)
        advanceUntilIdle()

        viewModel.state.test {
            assertEquals("GB", awaitItem().selectedCountry)

            // When - First change
            CountryManager.setCountry("FR")
            advanceUntilIdle()
            assertEquals("FR", awaitItem().selectedCountry)

            // When - Second change
            CountryManager.setCountry("JP")
            advanceUntilIdle()
            assertEquals("JP", awaitItem().selectedCountry)

            // When - Third change
            CountryManager.setCountry("AU")
            advanceUntilIdle()
            assertEquals("AU", awaitItem().selectedCountry)
        }
    }

    @Test
    fun `availableCountries should contain all supported countries`() = runTest(testDispatcher) {
        // Given
        CountryManager.setCountry("INIT")
        advanceUntilIdle()

        viewModel = PreferencesTabModel(fakePreferencesRepository)
        advanceUntilIdle()

        // When & Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(34, state.availableCountries.size) // We have 34 countries (including NONE)
            assertTrue(state.availableCountries.any { it.code == "US" })
            assertTrue(state.availableCountries.any { it.code == "BR" })
            assertTrue(state.availableCountries.any { it.code == "GB" })
        }
    }
}

// Fake implementation for testing
class FakePreferencesRepository : PreferencesRepository {
    private var savedLanguage: String? = null
    private var savedCountry: String? = null

    override suspend fun getLanguage(): String? = savedLanguage

    override suspend fun setLanguage(languageTag: String) {
        savedLanguage = languageTag
    }

    override suspend fun clearLanguage() {
        savedLanguage = null
    }

    override suspend fun getCountry(): String? = savedCountry

    override suspend fun setCountry(countryCode: String) {
        savedCountry = countryCode
    }

    override suspend fun clearCountry() {
        savedCountry = null
    }
}
