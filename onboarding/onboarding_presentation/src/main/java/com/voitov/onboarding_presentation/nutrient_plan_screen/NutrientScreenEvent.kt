package com.voitov.onboarding_presentation.nutrient_plan_screen

sealed class NutrientScreenEvent {
    sealed class OnValueEnter(val value: String): NutrientScreenEvent() {
        class OnFatRatioEnter(value: String) : OnValueEnter(value)
        class OnCarbRatioEnter(value: String) : OnValueEnter(value)
        class OnProteinsRatioEnter(value: String) : OnValueEnter(value)
    }
    object NavigateNext: NutrientScreenEvent()
}