package com.voitov.onboarding_presentation.nutrient_plan_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.utils.UiEvents
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common.domain.use_cases.FilterOutDigitsUseCase
import com.voitov.onboarding_domain.use_cases.HandleNutrientPlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientPlanViewModel @Inject constructor(
    private val keyValueStorage: UserInfoKeyValueStorage,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase,
    private val nutrientPlanUseCase: HandleNutrientPlanUseCase
) : ViewModel() {
    var screenState by mutableStateOf(NutrientScreenState("50", "30", "20"))
        private set

    private val _uiChannel = Channel<UiEvents>()
    val uiEvent = _uiChannel.receiveAsFlow()

    fun onEvent(event: NutrientScreenEvent) {
        if (event is NutrientScreenEvent.OnValueEnter) {
            if (event.value.length > 3) {
                return
            }
        }

        when (event) { // reducer
            is NutrientScreenEvent.OnValueEnter.OnCarbRatioEnter -> {
                screenState = screenState.copy(carbRatio = filterOutDigitsUseCase(event.value))
            }

            is NutrientScreenEvent.OnValueEnter.OnFatRatioEnter -> {
                screenState = screenState.copy(fatRatio = filterOutDigitsUseCase(event.value))
            }

            is NutrientScreenEvent.OnValueEnter.OnProteinsRatioEnter -> {
                screenState = screenState.copy(proteinRation = filterOutDigitsUseCase(event.value))
            }

            NutrientScreenEvent.NavigateNext -> {
                viewModelScope.launch {
                    val result = nutrientPlanUseCase(
                        carbsRatioText = screenState.carbRatio,
                        fatRatioText = screenState.fatRatio,
                        proteinRatioText = screenState.proteinRation
                    )
                    when (result) {
                        is HandleNutrientPlanUseCase.Result.Success -> {
                            keyValueStorage.saveCarbRatio(result.carbRatio)
                            keyValueStorage.saveFatRatio(result.fatRatio)
                            keyValueStorage.saveProteinRatio(result.proteinRatio)
                            _uiChannel.send(UiEvents.DispatchNavigationRequest)
                        }

                        is HandleNutrientPlanUseCase.Result.Error -> {
                            _uiChannel.send(UiEvents.ShowUpSnackBar(result.message))
                        }
                    }
                }
            }
        }
    }
}