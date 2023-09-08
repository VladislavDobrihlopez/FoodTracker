package com.voitov.tracker_presentation.trackable_food_manager_screen

import com.voitov.common.nav.TrackableFoodManagerSection

sealed class TrackableFoodManagerUiEvent {
    data class NavigateToSection(val section: TrackableFoodManagerSection) :
        TrackableFoodManagerUiEvent()
}