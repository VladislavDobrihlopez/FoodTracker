package com.voitov.tracker_presentation.trackable_food_manager_screen

import com.voitov.common.utils.UiText
import com.voitov.common.R
import com.voitov.common.nav.TrackableFoodManagerSection

data class TrackableFoodManagerScreenState(
    val sections: List<SectionUiModel>
)

val sectionsByDefault = listOf(
    SectionUiModel(
        isInWrappedState = true,
        action = UiText.StaticResource(R.string.section_search_action),
        description = UiText.StaticResource(R.string.section_search_description),
        section = TrackableFoodManagerSection.SEARCHING_FROM_EXTERNAL_OR_INTERNAL_FOOD_SECTION
    ),
    SectionUiModel(
        isInWrappedState = true,
        action = UiText.StaticResource(R.string.section_custom_food_manager_action),
        description = UiText.StaticResource(R.string.section_custom_food_manager_decsription),
        section = TrackableFoodManagerSection.ADDING_CUSTOM_FOOD_SECTION
    )
)