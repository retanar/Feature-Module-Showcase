package com.featuremodule.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface UiState
interface UiEvent

abstract class BaseVM<State : UiState, Event : UiEvent> : ViewModel() {
    private val _state: MutableStateFlow<State> by lazy { MutableStateFlow(initialState()) }
    /** State to be consumed by UI */
    val state = _state.asStateFlow()

    protected abstract fun initialState(): State

    // Extra capacity added to possibly make emit suspend less
    private val event = MutableSharedFlow<Event>(extraBufferCapacity = 1)

    /** Allows UI to send events to VM */
    fun postEvent(event: Event) = launch {
        this@BaseVM.event.emit(event)
    }

    init {
        // Run collection of events
        launch {
            event.collect(::handleEvent)
        }
    }

    /** Receives events in VM */
    protected abstract fun handleEvent(event: Event)

    /** Utility function mirroring viewModelScope.launch() */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(block = block)
}
