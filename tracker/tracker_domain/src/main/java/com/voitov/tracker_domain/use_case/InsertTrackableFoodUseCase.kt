package com.voitov.tracker_domain.use_case

import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import java.time.LocalDateTime
import kotlin.math.roundToInt

class InsertTrackableFoodUseCase(
    private val repository: FoodTrackerRepository
) {
    suspend operator fun invoke(
        food: TrackableFood,
        amount: Int,
        dateTime: LocalDateTime,
        mealType: MealType
    ) {
        repository.insertTrackedFood(
            TrackedFood(
                name = food.name,
                imageUrl = food.imageSourcePath,
                calories = ((food.caloriesPer100g * amount) / 100f).roundToInt(),
                carbs = ((food.carbsPer100g * amount) / 100f).roundToInt(),
                protein = ((food.proteinPer100g * amount) / 100f).roundToInt(),
                fat = ((food.fatProteinPer100g * amount) / 100f).roundToInt(),
                mealType = mealType,
                amount = amount,
                date = dateTime
            )
        )
    }
}