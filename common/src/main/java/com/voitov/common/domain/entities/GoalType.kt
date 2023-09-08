package com.voitov.common.domain.entities

import com.voitov.common.domain.FoodTrackerDomainException

sealed class GoalType(val name: String) {
    object LoseWeight : GoalType(LOSE_WEIGHT)
    object KeepWeight : GoalType(KEEP_WEIGHT)
    object GainWeight : GoalType(GAIN_WEIGHT)

    companion object {
        const val LOSE_WEIGHT = "lose_weight"
        const val KEEP_WEIGHT = "keep_weight"
        const val GAIN_WEIGHT = "gain_weight"

        fun parse(className: String): GoalType {
            return when (className) {
                LOSE_WEIGHT -> LoseWeight
                KEEP_WEIGHT -> KeepWeight
                GAIN_WEIGHT -> GainWeight
                else -> throw FoodTrackerDomainException.EncounteredUnknownPreferenceEntity()
            }
        }
    }
}