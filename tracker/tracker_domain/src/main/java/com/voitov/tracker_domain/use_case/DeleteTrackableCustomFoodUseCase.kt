package com.voitov.tracker_domain.use_case

import com.voitov.tracker_domain.model.CustomTrackableFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository

class DeleteTrackableCustomFoodUseCase(
    private val repository: FoodTrackerRepository
) {
    suspend operator fun invoke(food: CustomTrackableFood) {
        repository.deleteTrackableFood(food)
    }
}