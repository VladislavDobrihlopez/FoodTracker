package com.voitov.tracker_presentation.health_tracker_screen

import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_presentation.health_tracker_screen.model.Meal
import com.voitov.tracker_presentation.health_tracker_screen.model.allDayMealsByDefault
import java.time.LocalDateTime

data class HealthTrackerScreenState(
    val caloriesPerDayGoal: Int = 0,
    val caloriesPerDayInFact: Int = 0,
    val carbsPerDayGoal: Int = 0,
    val carbsPerDayInFact: Int = 0,
    val fatPerDayGoal: Int = 0,
    val fatPerDayInFact: Int = 0,
    val proteinsPerDayGoal: Int = 0,
    val proteinsPerDayInFact: Int = 0,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val trackedFoods: List<TrackedFood> = emptyList(),
    val mealsDuringCurrentDay: List<Meal> = allDayMealsByDefault
)

