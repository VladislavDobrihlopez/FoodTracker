package com.voitov.onboarding_presentation.welcome.gender_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.domain.entities.Gender
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common_ui.navigation.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(
    private val keyValueStorage: UserInfoKeyValueStorage
) : ViewModel() {
    var genderState by mutableStateOf<Gender>(Gender.Male)
        private set

    private val _uiEvent = Channel<UiEvents>()
    val uiEvent = _uiEvent
        .receiveAsFlow()

    fun onGenderClick(gender: Gender) {
        genderState = gender
    }

    fun onNavigate() {
        viewModelScope.launch {
            keyValueStorage.saveGender(genderState)
            _uiEvent.send(UiEvents.DispatchNavigationRequest)
        }
    }
}