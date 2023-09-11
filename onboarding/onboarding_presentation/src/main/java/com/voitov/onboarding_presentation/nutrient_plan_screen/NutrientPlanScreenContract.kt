package com.voitov.onboarding_presentation.nutrient_plan_screen

data class NutrientScreenState(
    val carbRatio: String,
    val proteinRation: String,
    val fatRatio: String
)

sealed class NutrientScreenEvent {
    sealed class OnValueEnter(val value: String): NutrientScreenEvent() {
        class OnFatRatioEnter(value: String) : OnValueEnter(value)
        class OnCarbRatioEnter(value: String) : OnValueEnter(value)
        class OnProteinsRatioEnter(value: String) : OnValueEnter(value)
    }
    object OnClickNavigationElement: NutrientScreenEvent()
}