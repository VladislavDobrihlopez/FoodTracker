package com.voitov.tracker_domain.use_case

import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class RetrieveAllTrackedFoodOnDateUseCase(
    private val repository: FoodTrackerRepository
) {
    operator fun invoke(date: LocalDateTime): Flow<List<TrackedFood>> {
        return repository.getFoodForDate(date)
    }
}