package com.itunesexplorer.foundation.mvi

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.core.logger.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface ViewState

interface ViewIntent

interface ViewEffect

/**
 * Represents the absence of effects in a ViewModel.
 * Use this when your ViewModel doesn't need to emit one-time side effects.
 *
 * Example:
 * ```
 * class MyScreenModel : MviViewModel<MyViewState, MyIntent, NoEffect>(...)
 * ```
 */
object NoEffect : ViewEffect

abstract class MviViewModel<State : ViewState, Intent : ViewIntent, Effect : ViewEffect>(
    initialState: State,
    protected val logger: Logger? = null
) : StateScreenModel<State>(initialState) {

    private val effectChannel = Channel<Effect>(Channel.BUFFERED)
    val effect: Flow<Effect> = effectChannel.receiveAsFlow()

    /**
     * Tag used for logging. Defaults to the simple class name.
     */
    protected open val logTag: String
        get() = this::class.simpleName ?: "MviViewModel"

    init {
        logger?.debug(logTag, "ViewModel initialized with state: $initialState")
    }

    abstract fun onAction(intent: Intent)

    /**
     * Logs the intent and calls the abstract onAction method.
     * This method is public and should be called from the UI.
     */
    fun dispatch(intent: Intent) {
        logger?.debug(logTag, "Intent received: $intent")
        onAction(intent)
    }

    protected fun sendEffect(effect: Effect) {
        logger?.debug(logTag, "Effect sent: $effect")
        screenModelScope.launch {
            effectChannel.send(effect)
        }
    }

    /**
     * Updates the state and logs the change.
     */
    protected fun updateState(block: (State) -> State) {
        val oldState = state.value
        mutableState.value = block(state.value)
        val newState = state.value
        logger?.debug(logTag, "State updated: $oldState -> $newState")
    }
}
