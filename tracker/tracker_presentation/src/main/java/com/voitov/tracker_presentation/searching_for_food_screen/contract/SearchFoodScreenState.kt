package com.voitov.tracker_presentation.searching_for_food_screen.contract

import com.voitov.common.R
import com.voitov.common.utils.UiText
import com.voitov.tracker_domain.model.TrackableFoodSearchingType
import com.voitov.tracker_presentation.searching_for_food_screen.model.SearchConfigUiModel
import com.voitov.tracker_presentation.searching_for_food_screen.model.TrackableFoodUiModel
import com.voitov.tracker_presentation.searching_for_food_screen.model.allCountriesByDefault

data class SearchFoodScreenState(
    val isSearchingGoingOn: Boolean = false,
    val isHintVisible: Boolean = false,
    val searchBarText: String = "",
    val countrySearchSettings: List<SearchConfigUiModel> = allCountriesByDefault,
    val tabs: Map<TabSection, TabSectionScreenState> = tabScreensState,
    val currentSelectedTab: TabSection = tabSections[0]
) {
    val tabSectionScreenState: TabSectionScreenState
        get() = tabs[currentSelectedTab] ?: throw IllegalStateException()
}

data class TabSection(
    val name: UiText,
    val section: TrackableFoodSearchingType,
)

data class TabSectionScreenState(
    val food: List<TrackableFoodUiModel> = emptyList(),
    val isSelected: Boolean
)

val tabSections = listOf(
    TabSection(
        name = UiText.StaticResource(R.string.section_internet),
        section = TrackableFoodSearchingType.INTERNET,
    ),
    TabSection(
        name = UiText.StaticResource(R.string.section_local),
        section = TrackableFoodSearchingType.LOCAL,
    ),
)
val tabScreensState = hashMapOf<TabSection, TabSectionScreenState>(
    Pair(
        tabSections[0],
        TabSectionScreenState(isSelected = true)
    ),
    Pair(
        tabSections[1],
        TabSectionScreenState(isSelected = false)
    ),
)