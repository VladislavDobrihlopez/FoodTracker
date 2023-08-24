package com.voitov.tracker_presentation.searching_for_food_screen.model

import com.voitov.tracker_domain.model.TrackableFood

data class TrackableFoodUiModel(
    val food: TrackableFood,
    val amount: String = "",
    val isExpanded: Boolean = false,
)