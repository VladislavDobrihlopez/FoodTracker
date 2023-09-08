package com.voitov.tracker_domain.use_case

import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository

class DeleteTrackedFoodUseCase(
    private val repository: FoodTrackerRepository
) {
    suspend operator fun invoke(food: TrackedFood) {
        repository.deleteTrackedFood(food)
    }
}