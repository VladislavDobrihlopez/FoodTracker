package com.voitov.common_ui

import android.content.Context

sealed class UiText {
    data class StaticResource(val resId: Int): UiText()
    data class DynamicResource(val text: String): UiText()

    fun asString(context: Context): String {
        return when(this) {
            is DynamicResource -> this.text
            is StaticResource -> context.getString(this.resId)
        }
    }
}