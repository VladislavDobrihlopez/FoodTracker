package com.voitov.tracker_domain.repository

import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.model.TrackedFood
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface FoodTrackerRepository {
    suspend fun searchForTrackableFood(
        query: String,
        page: Int,
        pageSize: Int,
        lowerBoundCoefficient: Float,
        upperBoundCoefficient: Float
    ): Result<List<TrackableFood>>

    suspend fun insertTrackedFood(item: TrackedFood)
    suspend fun deleteTrackedFood(item: TrackedFood)
    suspend fun restoreTrackedFood(item: TrackedFood)
    fun getFoodForDate(date: LocalDateTime): Flow<List<TrackedFood>>
}