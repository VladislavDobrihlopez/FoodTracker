package com.voitov.onboarding_presentation.weight_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.R
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common.domain.use_cases.FilterOutDigitsUseCase
import com.voitov.common_ui.UiEvents
import com.voitov.common_ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val keyValueStorage: UserInfoKeyValueStorage,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {
    var weightState by mutableStateOf<String>("70.0")
        private set

    private val _uiChannel = Channel<UiEvents>()
    val uiEvent = _uiChannel.receiveAsFlow()

    fun onChange(value: String) {
        if (value.length <= 4) {
            weightState = value //filterOutDigitsUseCase.invoke(value)
        }
    }

    fun onNavigate() {
        viewModelScope.launch {
            val userWeight = weightState.toFloatOrNull() ?: kotlin.run {
                _uiChannel.send(UiEvents.ShowUpSnackBar(UiText.StaticResource(R.string.error_height_cant_be_empty)))
                return@launch
            }

            keyValueStorage.saveWeight(userWeight)
            _uiChannel.send(UiEvents.DispatchNavigationRequest)
        }
    }
}