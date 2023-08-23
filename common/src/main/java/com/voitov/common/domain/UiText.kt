package com.voitov.common.domain

import android.content.Context

sealed class UiText {
    abstract fun isBlank(): Boolean

    data class StaticResource(val resId: Int): UiText() {
        override fun isBlank(): Boolean {
            return false // it is considered resources mustn't be empty when using them
        }
    }

    data class DynamicResource(val text: String): UiText() {
        override fun isBlank(): Boolean {
            return text.isBlank()
        }
    }


    fun asString(context: Context): String {
        return when(this) {
            is DynamicResource -> this.text
            is StaticResource -> context.getString(this.resId)
        }
    }
}