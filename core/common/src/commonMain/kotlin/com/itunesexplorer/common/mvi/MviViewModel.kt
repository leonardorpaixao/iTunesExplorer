package com.itunesexplorer.common.mvi

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface ViewState

interface ViewIntent

interface ViewEffect

abstract class MviViewModel<State : ViewState, Intent : ViewIntent, Effect : ViewEffect>(
    initialState: State
) : StateScreenModel<State>(initialState) {

    private val effectChannel = Channel<Effect>(Channel.BUFFERED)
    val effect: Flow<Effect> = effectChannel.receiveAsFlow()

    abstract fun onAction(intent: Intent)

    protected fun sendEffect(effect: Effect) {
        screenModelScope.launch {
            effectChannel.send(effect)
        }
    }
}
