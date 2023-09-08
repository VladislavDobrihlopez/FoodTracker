package com.voitov.common.domain.entities

import com.voitov.common.domain.FoodTrackerDomainException

sealed class Gender(val name: String) {
    object Male : Gender(MALE_GENDER)
    object Female : Gender(FEMALE_GENDER)

    companion object {
        const val MALE_GENDER = "male_gender"
        const val FEMALE_GENDER = "female_gender"

        fun parse(className: String): Gender {
            return when (className) {
                MALE_GENDER -> Male
                FEMALE_GENDER -> Female
                else -> throw FoodTrackerDomainException.EncounteredUnknownPreferenceEntity()
            }
        }
    }
}