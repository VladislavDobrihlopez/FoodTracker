package com.voitov.tracker_domain.use_case

import com.voitov.common.Configuration
import com.voitov.common.domain.FoodTrackerDomainException
import com.voitov.common.utils.UiText
import com.voitov.common.utils.areNutrientComponentsCorrect
import com.voitov.common.utils.calculateCalories
import com.voitov.common.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TryValidatingCustomFoodEnteredNutrientsUseCase @Inject constructor() {
    operator fun invoke(name: String, protein: String, fat: String, carbohydrates: String): Result {
        var mappedName = name
        var mappedProtein = 0
        var mappedFat = 0
        var mappedCarbohydrates = 0
        var mappedCalories = 0

        val passedTestValues = buildList<Boolean> {
            repeat(5) {
                add(true)
            }
        }.toMutableList()

        try {
            mappedName = name

            if (mappedName.isEmpty()) {
                passedTestValues[4] = false
                throw FoodTrackerDomainException.EnteredValueFormatException()
            }

            try {
                mappedProtein = protein.trim().toInt()
            } catch (ex: NumberFormatException) {
                passedTestValues[0] = false
                throw FoodTrackerDomainException.EnteredValueFormatException()
            }
            try {
                mappedFat = fat.trim().toInt()
            } catch (ex: NumberFormatException) {
                passedTestValues[1] = false
                throw FoodTrackerDomainException.EnteredValueFormatException()
            }
            try {
                mappedCarbohydrates = carbohydrates.trim().toInt()
            } catch (ex: NumberFormatException) {
                passedTestValues[2] = false
                throw FoodTrackerDomainException.EnteredValueFormatException()
            }
            try {
                mappedCalories = calculateCalories(
                    carbohydrates = mappedCarbohydrates,
                    fat = mappedFat,
                    protein = mappedProtein
                )
            } catch (ex: NumberFormatException) {
                passedTestValues[3] = false
                throw FoodTrackerDomainException.EnteredValueFormatException()
            }
        } catch (ex: FoodTrackerDomainException.EnteredValueFormatException) {
            return Result.Error(
                UiText.DynamicResource(ex.message.toString()),
                areProteinsIncorrect = !passedTestValues[0],
                areFatsIncorrect = !passedTestValues[1],
                areCarbsIncorrect = !passedTestValues[2],
                areCaloriesIncorrect = !passedTestValues[3],
                isNameIncorrect = !passedTestValues[4]
            )
        }

        return if (areNutrientComponentsCorrect(
                calories = mappedCalories,
                carbohydrates = mappedCarbohydrates,
                fat = mappedFat,
                protein = mappedProtein,
                lowerBoundCoefficient = Configuration.LOWER_BOUND,
                upperBoundCoefficient = Configuration.UPPER_BOUND
            )
        ) {
            Result.Success(
                productName = mappedName,
                carbsIn100g = mappedCarbohydrates,
                fatIn100g = mappedFat,
                proteinsIn100g = mappedProtein,
                caloriesIn100g = mappedCalories
            )
        } else {
            Result.Error(UiText.StaticResource(R.string.error_unknown))
        }
    }

    sealed class Result {
        data class Success(
            val productName: String,
            val carbsIn100g: Int,
            val fatIn100g: Int,
            val proteinsIn100g: Int,
            val caloriesIn100g: Int
        ) : Result()

        data class Error(
            val message: UiText,
            val isNameIncorrect: Boolean = false,
            val areCarbsIncorrect: Boolean = false,
            val areFatsIncorrect: Boolean = false,
            val areProteinsIncorrect: Boolean = false,
            val areCaloriesIncorrect: Boolean = false
        ) : Result()
    }
}