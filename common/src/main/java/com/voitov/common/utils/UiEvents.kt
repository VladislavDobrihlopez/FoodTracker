package com.voitov.common.utils

abstract class UiEvents {
    object DispatchNavigationRequest : UiEvents()
    object NavigateUp : UiEvents()
    data class ShowUpSnackBar(val text: UiText): UiEvents()
}