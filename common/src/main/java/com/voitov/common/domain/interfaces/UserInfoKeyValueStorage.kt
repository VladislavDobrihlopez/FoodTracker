package com.voitov.common.domain.interfaces

import com.voitov.common.domain.entities.Gender
import com.voitov.common.domain.entities.GoalType
import com.voitov.common.domain.entities.PhysicalActivityLevel
import com.voitov.common.domain.entities.UserProfile

interface UserInfoKeyValueStorage {
    fun saveGender(gender: Gender)
    fun saveGoal(goalType: GoalType)
    fun savePhysicalActivity(physicalActivityLevel: PhysicalActivityLevel)
    fun saveAge(value: Int)
    fun saveHeight(value: Int)
    fun saveWeight(value: Float)
    fun saveCarbRatio(value: Float)
    fun saveProteinRatio(value: Float)
    fun saveFatRatio(value: Float)
    fun readAllUserInfo(): UserProfile

    companion object {
        const val STORAGE_LOCATION = "user_info_shared_pref"
        const val GENDER_EXTRA_KEY = "GENDER_EXTRA_KEY"
        const val GOAL_TYPE_EXTRA_KEY = "GOAL_TYPE_EXTRA_KEY"
        const val PHYSICAL_ACTIVITY_LEVEL_EXTRA_KEY = "PHYSICAL_ACTIVITY_LEVEL_EXTRA_KEY"
        const val AGE_EXTRA_KEY = "AGE_EXTRA_KEY"
        const val WEIGHT_EXTRA_KEY = "WEIGHT_EXTRA_KEY"
        const val HEIGHT_EXTRA_KEY = "HEIGHT_EXTRA_KEY"
        const val FAT_RATION_EXTRA_KEY = "FAT_RATION_EXTRA_KEY"
        const val PROTEIN_RATION_EXTRA_KEY = "PROTEIN_RATION_EXTRA_KEY"
        const val CARB_RATION_EXTRA_KEY = "CARB_RATION_EXTRA_KEY"
    }
}