package com.voitov.tracker_data.repository

import com.voitov.tracker_data.local.db.TrackedFoodDao
import com.voitov.tracker_data.mapper.toTrackableFood
import com.voitov.tracker_data.mapper.toTrackedFood
import com.voitov.tracker_data.mapper.toTrackedFoodEntity
import com.voitov.tracker_data.remote.OpenFoodApiService
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class FoodTrackerRepositoryImpl(
    private val apiService: OpenFoodApiService,
    private val dao: TrackedFoodDao
) : FoodTrackerRepository {
    override suspend fun searchForTrackableFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val dto = apiService.searchForProducts(query = query, page = page, pageSize = pageSize)
            Result.success(dto.products.mapNotNull { it.toTrackableFood() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun insertTrackedFood(item: TrackedFood) {
        dao.insertTrackableFood(item.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(item: TrackedFood) {
        dao.deleteTrackedFood(item.toTrackedFoodEntity())
    }

    override fun getFoodForDate(date: LocalDateTime): Flow<List<TrackedFood>> {
        return dao.selectByDate(
            year = date.year,
            month = date.monthValue,
            dayOfMonth = date.dayOfMonth
        ).map { dbEntities ->
            dbEntities.map { it.toTrackedFood() }
        }
    }
}