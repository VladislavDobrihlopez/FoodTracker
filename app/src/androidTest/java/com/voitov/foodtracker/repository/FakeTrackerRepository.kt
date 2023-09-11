package com.voitov.foodtracker.repository

import android.util.Log
import com.voitov.tracker_domain.model.Country
import com.voitov.tracker_domain.model.CustomTrackableFood
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDateTime
import kotlin.random.Random

class FakeTrackerRepository: FoodTrackerRepository {
    var shouldSucceed: Boolean = true
    private val inMemoryDatabase = mutableListOf<TrackedFood>()
    private val inMemoryDatabaseForCustomFood = mutableListOf<CustomTrackableFood>()
    var apiSearchResults = listOf<TrackableFood>()

    private val dbFlow = MutableSharedFlow<List<TrackedFood>>(replay = 1)

    override suspend fun searchForTrackableFood(
        query: String,
        page: Int,
        pageSize: Int,
        country: Country,
        lowerBoundCoefficient: Float,
        upperBoundCoefficient: Float
    ): Result<List<TrackableFood>> {
        Log.d("TEST_E2E", "$query $page $pageSize $country")
        return if (shouldSucceed) {
            Result.success(apiSearchResults)
        } else {
            Result.failure(Throwable())
        }
    }

    override suspend fun insertTrackedFood(item: TrackedFood) {
        inMemoryDatabase.add(item.copy(id = Random.nextInt()))
        dbFlow.emit(inMemoryDatabase)
    }

    override suspend fun deleteTrackedFood(item: TrackedFood) {
        inMemoryDatabase.remove(item.copy(id = Random.nextInt()))
        dbFlow.emit(inMemoryDatabase)
    }

    override suspend fun restoreTrackedFood(item: TrackedFood) {
        insertTrackedFood(item)
    }

    override fun getFoodForDate(date: LocalDateTime): Flow<List<TrackedFood>> {
        return dbFlow
    }

    override suspend fun insertTrackableFood(item: CustomTrackableFood) {
        inMemoryDatabaseForCustomFood.add(item)
        dbFlow.emit(inMemoryDatabase)
    }

    override suspend fun deleteTrackableFood(item: CustomTrackableFood) {
        inMemoryDatabaseForCustomFood.remove(item)
        dbFlow.emit(inMemoryDatabase)
    }

    override fun searchForCustomFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Flow<List<CustomTrackableFood>> {
        TODO("Not yet implemented")
    }
}