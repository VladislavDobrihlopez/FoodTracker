package com.voitov.tracker_presentation.trackable_food_manager_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackableFoodManagerViewModel @Inject constructor() : ViewModel() {
    private val _uiChannel = Channel<TrackableFoodManagerUiEvent>()
    val uiEvent = _uiChannel.receiveAsFlow()

    var screenState by mutableStateOf(TrackableFoodManagerScreenState(sectionsByDefault))
        private set


    fun onEvent(event: TrackableFoodManagerScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is TrackableFoodManagerScreenEvent.OnCardContentClick -> {
                    screenState = screenState.copy(sections = screenState.sections.map {
                        if (it == event.sectionUiModel) {
                            it.copy(isInWrappedState = !it.isInWrappedState)
                        } else {
                            it.copy(isInWrappedState = true)
                        }
                    })
                }

                is TrackableFoodManagerScreenEvent.OnNavAgreementButtonClick -> {
                    _uiChannel.send(TrackableFoodManagerUiEvent.NavigateToSection(event.sectionUiModel.section))
                }
            }
        }
    }
}