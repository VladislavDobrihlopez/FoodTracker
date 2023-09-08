package com.voitov.tracker_domain.use_case

import com.voitov.tracker_domain.model.CustomTrackableFood
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.repository.FoodTrackerRepository
import java.time.LocalDateTime
import javax.inject.Inject

class InsertCustomTrackableFoodUseCase @Inject constructor(
    private val repository: FoodTrackerRepository
) {
    suspend operator fun invoke(
        food: TrackableFood,
        dateTime: LocalDateTime,
    ) {
        repository.insertTrackableFood(
            CustomTrackableFood(trackableFood = food, date = dateTime)
        )
    }
}