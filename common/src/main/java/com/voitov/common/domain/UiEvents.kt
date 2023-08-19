package com.voitov.common.domain

abstract class UiEvents {
    data class NavigateTo(val route: String) : UiEvents()
    object DispatchNavigationRequest : UiEvents()
    object NavigateUp : UiEvents()
    data class ShowUpSnackBar(val text: UiText): UiEvents()
}