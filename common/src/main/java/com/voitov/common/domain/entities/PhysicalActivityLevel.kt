package com.voitov.common.domain.entities

import com.voitov.common.domain.PreferencesIoException

sealed class PhysicalActivityLevel(val name: String) {
    object Low : PhysicalActivityLevel(LOW_ACTIVITY)
    object Medium : PhysicalActivityLevel(MEDIUM_ACTIVITY)
    object High : PhysicalActivityLevel(HIGH_ACTIVITY)

    companion object {
        const val LOW_ACTIVITY = "low_activity_level"
        const val MEDIUM_ACTIVITY = "medium_activity_level"
        const val HIGH_ACTIVITY = "high_activity_level"

        fun parse(className: String): PhysicalActivityLevel {
            return when (className) {
                LOW_ACTIVITY -> Low
                MEDIUM_ACTIVITY -> Medium
                HIGH_ACTIVITY -> High
                else -> throw PreferencesIoException.EncounteredUnknownPreferenceEntity()
            }
        }
    }
}