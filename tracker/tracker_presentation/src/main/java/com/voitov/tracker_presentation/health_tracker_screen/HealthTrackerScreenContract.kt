package com.voitov.tracker_presentation.health_tracker_screen

import com.voitov.tracker_domain.model.MealTimeType
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_presentation.health_tracker_screen.components.ScreenMode
import com.voitov.tracker_presentation.health_tracker_screen.model.TimePointResult
import com.voitov.tracker_presentation.health_tracker_screen.model.Meal
import com.voitov.tracker_presentation.health_tracker_screen.model.allDayMealsByDefault
import java.time.LocalDateTime

data class HealthTrackerScreenState(
    val headerState: NutrientOverviewHeaderState = NutrientOverviewHeaderState(),
    val chartState: NutrientChartState = NutrientChartState(),
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val trackedFoods: List<TrackedFood> = emptyList(),
    val areTopBarActionsExpanded: Boolean = false,
    val mealsDuringCurrentDay: List<Meal> = allDayMealsByDefault,
    val currentMode: ScreenMode = ScreenMode.HOME
)

data class NutrientOverviewHeaderState(
    val caloriesPerDayGoal: Int = 0,
    val caloriesPerDayInFact: Int = 0,
    val carbsPerDayGoal: Int = 0,
    val carbsPerDayInFact: Int = 0,
    val fatPerDayGoal: Int = 0,
    val fatPerDayInFact: Int = 0,
    val proteinsPerDayGoal: Int = 0,
    val proteinsPerDayInFact: Int = 0,
)

data class NutrientChartState(
    val dataPoints: List<TimePointResult> = listOf(),
    val showExceeding: Boolean = false,
    val showAvgValue: Boolean = false
)

sealed class HealthTrackerScreenEvent {
    object NavigateToPreviousDay : HealthTrackerScreenEvent()
    object NavigateToNextDay : HealthTrackerScreenEvent()
    object NavigateToWeekBehind : HealthTrackerScreenEvent()
    object NavigateToWeekAhead : HealthTrackerScreenEvent()
    object DoReonbording : HealthTrackerScreenEvent()
    object RestoreFoodItem : HealthTrackerScreenEvent()

    data class DeleteTrackableFoodFromBeingTracked(val foodItem: TrackedFood) :
        HealthTrackerScreenEvent()

    data class ToggleMeal(val mealTimeType: MealTimeType) : HealthTrackerScreenEvent()
    object ToggleTopBar : HealthTrackerScreenEvent()

    data class MoveOnToMode(val mode: ScreenMode) : HealthTrackerScreenEvent()
    data class ChangeChartConfig(val showExceeding: Boolean, val showAvg: Boolean) : HealthTrackerScreenEvent()
}
