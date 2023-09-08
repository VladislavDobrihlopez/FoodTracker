package com.voitov.tracker_presentation.trackable_food_manager_screen

import com.voitov.common.nav.TrackableFoodManagerSection
import com.voitov.common.utils.UiText

data class SectionUiModel(
    val isInWrappedState: Boolean,
    val action: UiText,
    val description: UiText,
    val section: TrackableFoodManagerSection
)
