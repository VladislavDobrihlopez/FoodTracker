package com.voitov.onboarding_presentation.goal_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.domain.entities.GoalType
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common.utils.UiSideEffect
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

    private val _uiSideEffect = Channel<UiSideEffect>()
    val uiEvent = _uiSideEffect
        .receiveAsFlow()

    fun onSelect(goalType: GoalType) {
        goalState = goalType
    }

    fun onNavigate() {
        viewModelScope.launch {
            keyValueStorage.saveGoal(goalState)
            _uiSideEffect.send(UiSideEffect.DispatchNavigationRequest)
        }
    }
}