package com.voitov.tracker_domain.use_case

import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class RetrieveAllFoodOnDateUseCase(
    private val repository: FoodTrackerRepository
) {
    operator fun invoke(date: LocalDateTime): Flow<List<TrackedFood>> {
        return repository.getFoodForDate(date)
    }
}