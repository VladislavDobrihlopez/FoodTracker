package com.voitov.tracker_domain.use_case

import com.voitov.common.Configuration
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository

class SearchFoodUseCase(
    private val repository: FoodTrackerRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 100,
        lowerBoundCoefficient: Float = Configuration.LOWER_BOUND,
        upperBoundCoefficient: Float = Configuration.UPPER_BOUND
    ): Result<List<TrackableFood>> {
        return if (query.isNotBlank()) {
            repository.searchForTrackableFood(query.trim(), page, pageSize, lowerBoundCoefficient, upperBoundCoefficient)
        } else {
            Result.success(emptyList())
        }
    }
}