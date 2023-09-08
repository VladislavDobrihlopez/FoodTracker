package com.voitov.tracker_presentation.custom_food_screen.contract

import android.net.Uri

data class CustomFoodScreenState(
    val imageUri: Uri? = null,
    val enteredName: String = "",
    val enteredCarbsPer100g: String = "",
    val enteredProteinPer100g: String = "",
    val enteredFatPer100g: String = "",
    val calculatedCalories: String = "",
    val helperText: String? = null,
    val isEnteredCarbsAmountIncorrect: Boolean = false,
    val isEnteredFatAmountIncorrect: Boolean = false,
    val isEnteredProteinAmountIncorrect: Boolean = false,
    val isEnteredNameIncorrect: Boolean = false
)

sealed class CustomFoodScreenEvent {
    sealed class OnValueEnter(val value: String) : CustomFoodScreenEvent() {
        class OnNameEnter(value: String): OnValueEnter(value)
        class OnFatRatioEnter(value: String) : OnValueEnter(value)
        class OnCarbRatioEnter(value: String) : OnValueEnter(value)
        class OnProteinsRatioEnter(value: String) : OnValueEnter(value)
    }

    data class OnPhotoPicked(val uri: Uri): CustomFoodScreenEvent()
    object OnSaveButtonClick : CustomFoodScreenEvent()
}


