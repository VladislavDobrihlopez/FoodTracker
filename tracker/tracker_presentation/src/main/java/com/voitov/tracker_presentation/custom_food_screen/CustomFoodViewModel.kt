package com.voitov.tracker_presentation.custom_food_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.R
import com.voitov.common.utils.UiSideEffect
import com.voitov.common.utils.UiText
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.use_case.InsertCustomTrackableFoodUseCase
import com.voitov.tracker_domain.use_case.TryValidatingCustomFoodEnteredNutrientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CustomFoodViewModel @Inject constructor(
    private val tryValidatingCustomFoodEnteredNutrientsUseCase: TryValidatingCustomFoodEnteredNutrientsUseCase,
    private val insertCustomTrackableFoodUseCase: InsertCustomTrackableFoodUseCase
) : ViewModel() {
    private val _uiChannel = Channel<UiSideEffect>()
    val uiEvents = _uiChannel.receiveAsFlow()
    var screenState by mutableStateOf(CustomFoodScreenState())

    fun onEvent(event: CustomFoodScreenEvent) {
        viewModelScope.launch {
            when (event) {
                CustomFoodScreenEvent.OnSaveButtonClick -> {
                    val result = tryValidatingCustomFoodEnteredNutrientsUseCase(
                        name = screenState.enteredName,
                        protein = screenState.enteredProteinPer100g,
                        fat = screenState.enteredFatPer100g,
                        carbohydrates = screenState.enteredCarbsPer100g
                    )

                    if (screenState.imageUri === null) {
                        _uiChannel.send(UiSideEffect.ShowUpSnackBar(UiText.StaticResource(R.string.you_should_set_image)))
                    } else if (result is TryValidatingCustomFoodEnteredNutrientsUseCase.Result.Error) {
                        screenState = screenState.copy(
                            isEnteredNameIncorrect = result.isNameIncorrect,
                            isEnteredCarbsAmountIncorrect = result.areCarbsIncorrect,
                            isEnteredFatAmountIncorrect = result.areFatsIncorrect,
                            isEnteredProteinAmountIncorrect = result.areProteinsIncorrect
                        )
                        _uiChannel.send(UiSideEffect.ShowUpSnackBar(result.message))
                    } else if (result is TryValidatingCustomFoodEnteredNutrientsUseCase.Result.Success) {
                        insertCustomTrackableFoodUseCase(
                            food = TrackableFood(
                                name = result.productName,
                                imageSourcePath = screenState.imageUri.toString(),
                                caloriesPer100g = result.caloriesIn100g,
                                carbsPer100g = result.carbsIn100g,
                                proteinPer100g = result.proteinsIn100g,
                                fatProteinPer100g = result.fatIn100g,
                                id = ""
                            ), dateTime = LocalDateTime.now()
                        )
                        _uiChannel.send(UiSideEffect.NavigateUp)
                    }
                }

                is CustomFoodScreenEvent.OnValueEnter.OnCarbRatioEnter -> {
                    screenState = screenState.copy(enteredCarbsPer100g = event.value.trim())
                    tryDisplayingCalculatedCalories()
                }

                is CustomFoodScreenEvent.OnValueEnter.OnFatRatioEnter -> {
                    screenState = screenState.copy(enteredFatPer100g = event.value.trim())
                    tryDisplayingCalculatedCalories()
                }

                is CustomFoodScreenEvent.OnValueEnter.OnNameEnter -> {
                    screenState = screenState.copy(enteredName = event.value.trim())
                    tryDisplayingCalculatedCalories()
                }

                is CustomFoodScreenEvent.OnValueEnter.OnProteinsRatioEnter -> {
                    screenState = screenState.copy(enteredProteinPer100g = event.value.trim())
                    tryDisplayingCalculatedCalories()
                }

                is CustomFoodScreenEvent.OnPhotoPicked -> {
                    screenState = screenState.copy(imageUri = event.uri)
                }
            }
        }
    }

    private fun tryDisplayingCalculatedCalories() {
        val result = tryValidatingCustomFoodEnteredNutrientsUseCase(
            name = screenState.enteredName,
            protein = screenState.enteredProteinPer100g,
            fat = screenState.enteredFatPer100g,
            carbohydrates = screenState.enteredCarbsPer100g
        )

        if (result is TryValidatingCustomFoodEnteredNutrientsUseCase.Result.Success) {
            screenState =
                screenState.copy(
                    calculatedCalories = result.caloriesIn100g.toString(),
                    helperText = "4 * ${result.carbsIn100g} + 4 * ${result.proteinsIn100g} + 9 * ${result.fatIn100g} = ${result.caloriesIn100g}"
                )
        }
    }
}