package com.voitov.tracker_presentation.searching_for_food_screen

import com.voitov.common.domain.UiText
import com.voitov.tracker_presentation.searching_for_food_screen.model.TrackableFoodUiModel

data class SearchFoodScreenState(
    val food: List<TrackableFoodUiModel> = emptyList(),
    val searchBarText: UiText = UiText.DynamicResource(""),
    val isHintVisible: Boolean = false,
    val isSearchingGoingOn: Boolean = false
)
