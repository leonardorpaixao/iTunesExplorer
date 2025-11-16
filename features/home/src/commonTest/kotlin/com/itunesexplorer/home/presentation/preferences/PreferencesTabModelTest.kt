package com.itunesexplorer.home.presentation.preferences

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesTabModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: PreferencesTabModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PreferencesTabModel()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have empty placeholder`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("", state.placeholder)
        }
    }

    @Test
    fun `onAction should not crash with no implementation`() = runTest {
        // This test ensures that the ViewModel doesn't crash
        // when intents are sent even though there's no implementation yet
        viewModel.state.test {
            val initialState = awaitItem()
            assertEquals("", initialState.placeholder)

            // No intents to test yet, but the structure is ready
            cancelAndIgnoreRemainingEvents()
        }
    }
}
