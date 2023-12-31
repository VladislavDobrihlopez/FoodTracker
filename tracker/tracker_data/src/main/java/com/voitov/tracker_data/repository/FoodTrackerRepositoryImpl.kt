package com.voitov.tracker_data.repository

import com.voitov.common.utils.areNutrientComponentsCorrect
import com.voitov.tracker_data.local.db.TrackedFoodDao
import com.voitov.tracker_data.mapper.toCustomTrackableFood
import com.voitov.tracker_data.mapper.toFoodStatistics
import com.voitov.tracker_data.mapper.toTrackableFood
import com.voitov.tracker_data.mapper.toTrackableFoodEntity
import com.voitov.tracker_data.mapper.toTrackedFood
import com.voitov.tracker_data.mapper.toTrackedFoodEntity
import com.voitov.tracker_data.remote.BaseCountryHolder
import com.voitov.tracker_data.remote.OpenFoodApiService
import com.voitov.tracker_data.remote.query
import com.voitov.tracker_domain.model.Country
import com.voitov.tracker_domain.model.CustomTrackableFood
import com.voitov.tracker_domain.model.FoodStatistics
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.math.roundToInt

class FoodTrackerRepositoryImpl(
    private val apiService: OpenFoodApiService,
    private val dao: TrackedFoodDao,
    private val countryUrlHolders: List<BaseCountryHolder>
) : FoodTrackerRepository {
    override suspend fun searchForTrackableFood(
        query: String,
        page: Int,
        pageSize: Int,
        country: Country,
        lowerBoundCoefficient: Float,
        upperBoundCoefficient: Float
    ): Result<List<TrackableFood>> {
        return try {
            val url = countryUrlHolders.find { it.isRelativeType(country) }
                ?.replaceCountry(OpenFoodApiService.BASE_URL_FOR_FORMATTING)
                ?.query(OpenFoodApiService.SEARCH_TERMS_QUERY, query)
                ?.query(OpenFoodApiService.PAGE_QUERY, page)
                ?.query(OpenFoodApiService.PAGE_SIZE_QUERY, pageSize)
                ?: throw IllegalStateException("Have you forgotten to handle the ${country.name}?")

            val dto = apiService.searchForProducts(url = url)
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
            dao.insertTrackedFood(item.toTrackedFoodEntity())
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
            delay(450)
        }
    }

    override suspend fun insertTrackableFood(item: CustomTrackableFood) {
        withContext(Dispatchers.IO) {
            dao.insertCustomTrackableFood(item.toTrackableFoodEntity())
        }
    }

    override suspend fun deleteTrackableFood(item: CustomTrackableFood) {
        withContext(Dispatchers.IO) {
            dao.deleteCustomTrackableFood(item.toTrackableFoodEntity().id)
        }
    }

    override fun searchForCustomFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Flow<List<CustomTrackableFood>> {
        return dao.getAllCustomTrackableFood()
            .map { dbEntities ->
                dbEntities.map { it.toCustomTrackableFood() }
            }
    }

    override fun getAllTrackedFood(): Flow<List<TrackedFood>> {
        return dao.getAllTrackedFood()
            .map { dbEntities ->
                dbEntities.map { it.toTrackedFood() }
            }
    }
}