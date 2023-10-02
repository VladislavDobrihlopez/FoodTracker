package com.voitov.tracker_domain.use_case

import com.voitov.common.domain.entities.Gender
import com.voitov.common.domain.entities.GoalType
import com.voitov.common.domain.entities.PhysicalActivityLevel
import com.voitov.common.domain.entities.UserProfile
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.tracker_domain.model.MealTimeType
import com.voitov.tracker_domain.model.TrackedFood
import kotlin.math.roundToInt

class DoNutrientMathUseCase(
    private val keyValueStorage: UserInfoKeyValueStorage,
) {
    operator fun invoke(trackedFood: List<TrackedFood>): Result {
        val mealTimeToNutrients = trackedFood.groupBy {
            it.mealTimeType
        }.mapValues { entry ->
            NutrientsHolder(
                mealTimeType = entry.key,
                carbs = entry.value.sumOf { it.carbs },
                calories = entry.value.sumOf { it.calories },
                fat = entry.value.sumOf { it.fat },
                proteins = entry.value.sumOf { it.protein },
            )
        }

        val eatenCalories = trackedFood.sumOf { it.calories }
        val eatenCarbs = trackedFood.sumOf { it.carbs }
        val eatenFat = trackedFood.sumOf { it.fat }
        val eatenProtein = trackedFood.sumOf { it.protein }

        val userProfile = keyValueStorage.readAllUserInfo()

        val goalCalories = dailyCaloriesRequirement(userProfile)
        val goalCarbs = (goalCalories / 4f).roundToInt()
        val goalFat = (goalCalories / 4f).roundToInt()
        val goalProteins = (goalCalories / 9f).roundToInt()

        return Result(
            caloriesPerDayGoal = goalCalories,
            caloriesPerDayInFact = eatenCalories,
            carbsPerDayGoal = goalCarbs,
            carbsPerDayInFact = eatenCarbs,
            fatPerDayGoal = goalFat,
            fatPerDayInFact = eatenFat,
            proteinsPerDayGoal = goalProteins,
            proteinsPerDayInFact = eatenProtein,
            mealTimeToNutrients = mealTimeToNutrients
        )
    }

    data class Result(
        val caloriesPerDayGoal: Int,
        val caloriesPerDayInFact: Int,
        val carbsPerDayGoal: Int,
        val carbsPerDayInFact: Int,
        val fatPerDayGoal: Int,
        val fatPerDayInFact: Int,
        val proteinsPerDayGoal: Int,
        val proteinsPerDayInFact: Int,
        val mealTimeToNutrients: Map<MealTimeType, NutrientsHolder>
    )

    data class NutrientsHolder(
        val mealTimeType: MealTimeType,
        val carbs: Int,
        val calories: Int,
        val fat: Int,
        val proteins: Int,
    )

    private fun bmr(userProfile: UserProfile): Int {
        return when (userProfile.gender) {
            is Gender.Male -> {
                (66.47f + 13.75f * userProfile.weight +
                        5f * userProfile.height - 6.75f * userProfile.age).roundToInt()
            }

            is Gender.Female -> {
                (665.09f + 9.56f * userProfile.weight +
                        1.84f * userProfile.height - 4.67 * userProfile.age).roundToInt()
            }
        }
    }

    private fun dailyCaloriesRequirement(userProfile: UserProfile): Int {
        val activityFactor = when (userProfile.physicalActivityLevel) {
            is PhysicalActivityLevel.Low -> 1.2f
            is PhysicalActivityLevel.Medium -> 1.3f
            is PhysicalActivityLevel.High -> 1.4f
        }
        val caloriesExtra = when (userProfile.goalType) {
            is GoalType.LoseWeight -> -500
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 500
        }
        return (bmr(userProfile) * activityFactor + caloriesExtra).roundToInt()
    }
}