package com.voitov.onboarding_presentation.nutrient_plan_screen

sealed class NutrientScreenEvent {
    data class OnFatRatioEnter(val value: String): NutrientScreenEvent()
    data class OnCarbRatioEnter(val value: String): NutrientScreenEvent()
    data class OnProteinsRatioEnter(val value: String): NutrientScreenEvent()
    object NavigateNext: NutrientScreenEvent()
}