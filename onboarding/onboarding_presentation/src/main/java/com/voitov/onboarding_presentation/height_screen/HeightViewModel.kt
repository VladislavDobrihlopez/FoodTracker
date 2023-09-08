package com.voitov.onboarding_presentation.height_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.R
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common.domain.use_cases.FilterOutDigitsUseCase
import com.voitov.common.utils.UiSideEffect
import com.voitov.common.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeightViewModel @Inject constructor(
    private val keyValueStorage: UserInfoKeyValueStorage,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {
    var heightState by mutableStateOf<String>("175")
        private set

    private val _uiChannel = Channel<UiSideEffect>()
    val uiEvent = _uiChannel.receiveAsFlow()

    fun onChange(value: String) {
        if (value.length <= 3) {
            heightState = filterOutDigitsUseCase.invoke(value)
        }
    }

    fun onNavigate() {
        viewModelScope.launch {
            val userHeight = heightState.toIntOrNull() ?: kotlin.run {
                _uiChannel.send(UiSideEffect.ShowUpSnackBar(UiText.StaticResource(R.string.error_height_cant_be_empty)))
                return@launch
            }

            keyValueStorage.saveHeight(userHeight)
            _uiChannel.send(UiSideEffect.DispatchNavigationRequest)
        }
    }
}