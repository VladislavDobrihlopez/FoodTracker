package com.voitov.tracker_presentation.health_tracker_screen

import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_presentation.health_tracker_screen.model.Meal

sealed class HealthTrackerScreenEvent {
    object NavigateToPreviousDay : HealthTrackerScreenEvent()
    object NavigateToNextDay : HealthTrackerScreenEvent()
    object NavigateToWeekBehind : HealthTrackerScreenEvent()
    object NavigateToWeekAhead : HealthTrackerScreenEvent()
    object DoReonbording : HealthTrackerScreenEvent()
    data class AddTrackableFoodToBeingTracked(val meal: Meal) :
        HealthTrackerScreenEvent()

    data class DeleteTrackableFoodFromBeingTracked(val foodItem: TrackedFood) :
        HealthTrackerScreenEvent()

    data class ToggleMeal(val mealType: MealType) : HealthTrackerScreenEvent()
}
