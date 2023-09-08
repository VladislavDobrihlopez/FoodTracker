package com.voitov.tracker_presentation.searching_for_food_screen.mapper

import com.voitov.tracker_domain.model.CustomTrackableFood
import com.voitov.tracker_presentation.searching_for_food_screen.model.TrackableFoodUiModel
import java.time.LocalDateTime

fun TrackableFoodUiModel.toCustomTrackableFood(date: LocalDateTime): CustomTrackableFood {
    return CustomTrackableFood(trackableFood = this.food, date = date)
}