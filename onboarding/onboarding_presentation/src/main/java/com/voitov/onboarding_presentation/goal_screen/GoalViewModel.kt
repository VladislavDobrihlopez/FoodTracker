package com.voitov.onboarding_presentation.goal_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.domain.entities.GoalType
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common.domain.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val keyValueStorage: UserInfoKeyValueStorage
) : ViewModel() {
    var goalState by mutableStateOf<GoalType>(GoalType.KeepWeight)
        private set

    private val _uiEvent = Channel<UiEvents>()
    val uiEvent = _uiEvent
        .receiveAsFlow()

    fun onSelect(goalType: GoalType) {
        goalState = goalType
    }

    fun onNavigate() {
        viewModelScope.launch {
            keyValueStorage.saveGoal(goalState)
            _uiEvent.send(UiEvents.DispatchNavigationRequest)
        }
    }
}