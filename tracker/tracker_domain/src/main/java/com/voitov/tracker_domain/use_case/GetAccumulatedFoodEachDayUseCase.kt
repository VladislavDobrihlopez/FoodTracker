package com.voitov.tracker_domain.use_case

import com.voitov.common.utils.calculateCalories
import com.voitov.tracker_domain.model.FoodStatistics
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAccumulatedFoodEachDayUseCase(
    private val repository: FoodTrackerRepository
) {
    operator fun invoke(): Flow<List<FoodStatistics>> {
        return repository.getAllTrackedFood().map { trackedFood ->
            val grouped = trackedFood.groupingBy { it.date.toLocalDate() }
            val proteins =
                grouped.fold(0) { accumulator: Int, element: TrackedFood -> accumulator + element.proteins }
            val carbs =
                grouped.fold(0) { accumulator: Int, element: TrackedFood -> accumulator + element.carbs }
            val fats =
                grouped.fold(0) { accumulator: Int, element: TrackedFood -> accumulator + element.fats }
            val uniqueDates =
                trackedFood.distinctBy { it.date.toLocalDate() }.map { it.date.toLocalDate() }

            buildList<FoodStatistics> {
                uniqueDates.forEachIndexed { index, date ->
                    add(
                        FoodStatistics(
                            date = date,
                            inTotalKkal = calculateCalories(
                                carbohydrates = carbs[date]!!,
                                protein = proteins[date]!!,
                                fat = fats[date]!!
                            ),
                            fats = fats[date]!!,
                            carbs = carbs[date]!!,
                            proteins = proteins[date]!!
                        )
                    )
                }
            }.sortedByDescending { it.date }
        }
    }
}