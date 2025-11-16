package com.itunesexplorer.home.presentation

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
class HomeScreenModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeScreenModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeScreenModel()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have ALBUMS tab selected`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(HomeTab.ALBUMS, state.selectedTab)
        }
    }

    @Test
    fun `onAction SelectTab should update selected tab to SEARCH`() = runTest {
        viewModel.state.test {
            // Initial state
            assertEquals(HomeTab.ALBUMS, awaitItem().selectedTab)

            // Change to SEARCH
            viewModel.onAction(HomeIntent.SelectTab(HomeTab.SEARCH))
            assertEquals(HomeTab.SEARCH, awaitItem().selectedTab)
        }
    }

    @Test
    fun `onAction SelectTab should update selected tab to PREFERENCES`() = runTest {
        viewModel.state.test {
            // Initial state
            assertEquals(HomeTab.ALBUMS, awaitItem().selectedTab)

            // Change to PREFERENCES
            viewModel.onAction(HomeIntent.SelectTab(HomeTab.PREFERENCES))
            assertEquals(HomeTab.PREFERENCES, awaitItem().selectedTab)
        }
    }

    @Test
    fun `onAction SelectTab should switch between tabs correctly`() = runTest {
        viewModel.state.test {
            // Initial state
            assertEquals(HomeTab.ALBUMS, awaitItem().selectedTab)

            // Change to SEARCH
            viewModel.onAction(HomeIntent.SelectTab(HomeTab.SEARCH))
            assertEquals(HomeTab.SEARCH, awaitItem().selectedTab)

            // Change to PREFERENCES
            viewModel.onAction(HomeIntent.SelectTab(HomeTab.PREFERENCES))
            assertEquals(HomeTab.PREFERENCES, awaitItem().selectedTab)

            // Change back to ALBUMS
            viewModel.onAction(HomeIntent.SelectTab(HomeTab.ALBUMS))
            assertEquals(HomeTab.ALBUMS, awaitItem().selectedTab)
        }
    }
}
