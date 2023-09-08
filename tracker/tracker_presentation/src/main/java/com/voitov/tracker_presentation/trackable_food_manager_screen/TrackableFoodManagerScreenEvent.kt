package com.voitov.tracker_presentation.trackable_food_manager_screen

sealed class TrackableFoodManagerScreenEvent {
    data class OnCardContentClick(val sectionUiModel: SectionUiModel): TrackableFoodManagerScreenEvent()
    data class OnNavAgreementButtonClick(val sectionUiModel: SectionUiModel): TrackableFoodManagerScreenEvent()
}