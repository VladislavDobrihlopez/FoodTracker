package com.voitov.common.utils

abstract class UiSideEffect {
    object DispatchNavigationRequest : UiSideEffect()
    object NavigateUp : UiSideEffect()
    data class ShowUpSnackBar(val text: UiText): UiSideEffect()
}