package com.voitov.onboarding_presentation.age_screen

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
class AgeViewModel @Inject constructor(
    private val keyValueStorage: UserInfoKeyValueStorage,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {
    var ageState by mutableStateOf<String>(AGE_BY_DEFAULT)
        private set

    private val _uiChannel = Channel<UiSideEffect>()
    val uiEvent = _uiChannel.receiveAsFlow()

    fun onChange(value: String) {
        if (value.length <= 3) {
            ageState = filterOutDigitsUseCase.invoke(value)
        }
    }

    fun onNavigate() {
        viewModelScope.launch {
            val userAge = ageState.toIntOrNull() ?: kotlin.run {
                _uiChannel.send(UiSideEffect.ShowUpSnackBar(UiText.StaticResource(R.string.error_age_cant_be_empty)))
                return@launch
            }

            keyValueStorage.saveAge(userAge)
            _uiChannel.send(UiSideEffect.DispatchNavigationRequest)
        }
    }

    companion object {
        private const val AGE_BY_DEFAULT = "25"
    }
}