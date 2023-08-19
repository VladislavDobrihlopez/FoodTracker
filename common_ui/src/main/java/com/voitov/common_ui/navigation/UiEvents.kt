package com.voitov.common_ui.navigation

abstract class UiEvents {
    data class NavigateTo(val route: String) : UiEvents()
    object DispatchNavigationRequest : UiEvents()
    object NavigateUp : UiEvents()
}