package com.voitov.common_ui.navigation

sealed class UiEvents {
    data class NavigateTo(val route: String): UiEvents()
    object NavigateUp: UiEvents()
}