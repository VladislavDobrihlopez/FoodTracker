package com.voitov.onboarding_presentation.activity_level_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.domain.entities.PhysicalActivityLevel
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common.domain.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityLevelViewModel @Inject constructor(
    private val keyValueStorage: UserInfoKeyValueStorage
) : ViewModel() {
    var activityLevelState by mutableStateOf<PhysicalActivityLevel>(PhysicalActivityLevel.Medium)
        private set

    private val _uiEvent = Channel<UiEvents>()
    val uiEvent = _uiEvent
        .receiveAsFlow()

    fun onSelect(activityLevel: PhysicalActivityLevel) {
        activityLevelState = activityLevel
    }

    fun onNavigate() {
        viewModelScope.launch {
            keyValueStorage.savePhysicalActivity(activityLevelState)
            _uiEvent.send(UiEvents.DispatchNavigationRequest)
        }
    }
}