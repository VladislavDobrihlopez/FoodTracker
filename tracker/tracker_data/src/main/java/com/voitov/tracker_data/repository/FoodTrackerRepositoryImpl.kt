package com.voitov.tracker_data.repository

import com.voitov.common.utils.areNutrientComponentsCorrect
import com.voitov.tracker_data.local.db.TrackedFoodDao
import com.voitov.tracker_data.mapper.toTrackableFood
import com.voitov.tracker_data.mapper.toTrackedFood
import com.voitov.tracker_data.mapper.toTrackedFoodEntity
import com.voitov.tracker_data.remote.OpenFoodApiService
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.time.LocalDateTime
import kotlin.math.roundToInt

class FoodTrackerRepositoryImpl(
    private val apiService: OpenFoodApiService,
    private val dao: TrackedFoodDao
) : FoodTrackerRepository {
    override suspend fun searchForTrackableFood(
        query: String,
        page: Int,
        pageSize: Int,
        lowerBoundCoefficient: Float,
        upperBoundCoefficient: Float
    ): Result<List<TrackableFood>> {
        return try {
            val dto = apiService.searchForProducts(query = query, page = page, pageSize = pageSize)
            Result.success(dto.products.filter { serverDtoModel ->
                val nutriments = serverDtoModel.nutriments
                areNutrientComponentsCorrect(
                    calories = nutriments.energyKcal100g.roundToInt(),
                    carbohydrates = nutriments.carbohydrates100g.roundToInt(),
                    fat = nutriments.fat100g.roundToInt(),
                    protein = nutriments.proteins100g.roundToInt(),
                    lowerBoundCoefficient = lowerBoundCoefficient,
                    upperBoundCoefficient = upperBoundCoefficient
                )
            }.mapNotNull { it.toTrackableFood() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun insertTrackedFood(item: TrackedFood) {
        withContext(Dispatchers.IO) {
            dao.insertTrackableFood(item.toTrackedFoodEntity())
        }
    }

    override suspend fun deleteTrackedFood(item: TrackedFood) {
        withContext(Dispatchers.IO) {
            dao.deleteTrackedFood(item.toTrackedFoodEntity())
        }
    }

    override suspend fun restoreTrackedFood(item: TrackedFood) {
        insertTrackedFood(item)
    }

    override fun getFoodForDate(date: LocalDateTime): Flow<List<TrackedFood>> {
        return dao.selectByDate(
            year = date.year,
            month = date.monthValue,
            dayOfMonth = date.dayOfMonth
        ).map { dbEntities ->
            dbEntities.map { it.toTrackedFood() }
        }.onStart {
            delay(500)
        }
    }
}