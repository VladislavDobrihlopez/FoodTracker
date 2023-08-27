package com.voitov.tracker_presentation.searching_for_food_screen

import com.voitov.common.utils.UiText
import com.voitov.tracker_presentation.searching_for_food_screen.model.SearchConfigUiModel
import com.voitov.tracker_presentation.searching_for_food_screen.model.TrackableFoodUiModel
import com.voitov.tracker_presentation.searching_for_food_screen.model.allCountriesByDefault

data class SearchFoodScreenState(
    val food: List<TrackableFoodUiModel> = emptyList(),
    val searchBarText: UiText = UiText.DynamicResource(""),
    val isHintVisible: Boolean = false,
    val isSearchingGoingOn: Boolean = false,
    val countrySearchSettings: List<SearchConfigUiModel> = allCountriesByDefault
)