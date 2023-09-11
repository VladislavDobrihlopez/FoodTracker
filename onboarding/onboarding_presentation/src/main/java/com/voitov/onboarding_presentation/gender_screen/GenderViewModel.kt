package com.voitov.onboarding_presentation.gender_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.domain.entities.Gender
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common.utils.UiSideEffect
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

    private val _uiSideEffect = Channel<UiSideEffect>()
    val uiEvent = _uiSideEffect
        .receiveAsFlow()

    fun onSelect(gender: Gender) {
        genderState = gender
    }

    fun onNavigate() {
        viewModelScope.launch {
            keyValueStorage.saveGender(genderState)
            _uiSideEffect.send(UiSideEffect.DispatchNavigationRequest)
        }
    }
}