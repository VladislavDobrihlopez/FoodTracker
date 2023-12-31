package com.voitov.tracker_domain.use_case

import android.util.Log
import com.voitov.common.Configuration
import com.voitov.tracker_domain.model.Country
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import java.util.UUID

class SearchTrackableFoodUseCase(
    private val repository: FoodTrackerRepository
) {
    suspend operator fun invoke(
        query: String,
        country: Country,
        page: Int = 1,
        pageSize: Int = 100,
        lowerBoundCoefficient: Float = Configuration.LOWER_BOUND,
        upperBoundCoefficient: Float = Configuration.UPPER_BOUND
    ): Result<List<TrackableFood>> {
        Log.d("TEST_E2E", "usecase $query $page $pageSize $country")

        return if (query.isNotBlank()) {
            repository.searchForTrackableFood(
                query.trim(),
                page,
                pageSize,
                country,
                lowerBoundCoefficient,
                upperBoundCoefficient
            )
                .map { it -> it.map { Log.d("TEST_E2E", "$it")
                    it.copy(id = UUID.randomUUID().toString()) } }

        } else {
            Result.success(emptyList())
        }
    }
}