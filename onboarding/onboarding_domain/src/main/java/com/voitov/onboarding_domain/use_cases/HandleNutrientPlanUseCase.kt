package com.voitov.onboarding_domain.use_cases

import com.voitov.common.R
import com.voitov.common.utils.UiText
import javax.inject.Inject

class HandleNutrientPlanUseCase @Inject constructor() {
    operator fun invoke(
        carbsRatioText: String,
        fatRatioText: String,
        proteinRatioText: String
    ): Result {
        val carbsRatio = carbsRatioText.toIntOrNull() ?: return incorrectType
        val fatRatio = fatRatioText.toIntOrNull() ?: return incorrectType
        val proteinRatio = proteinRatioText.toIntOrNull() ?: return incorrectType

        return if (proteinRatio + fatRatio + carbsRatio != 100) {
            Result.Error(UiText.StaticResource(R.string.error_not_100_percent))
        } else {
            Result.Success(
                carbRatio = carbsRatio / 100f,
                fatRatio = fatRatio / 100f,
                proteinRatio = proteinRatio / 100f
            )
        }
    }

    private val incorrectType = Result.Error(UiText.StaticResource(R.string.error_invalid_values))

    sealed class Result {
        data class Success(val carbRatio: Float, val fatRatio: Float, val proteinRatio: Float) :
            Result()

        data class Error(val message: UiText) : Result()
    }
}