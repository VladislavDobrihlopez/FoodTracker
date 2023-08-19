package com.voitov.common.data

import android.content.SharedPreferences
import com.voitov.common.domain.entities.Gender
import com.voitov.common.domain.entities.GoalType
import com.voitov.common.domain.entities.PhysicalActivityLevel
import com.voitov.common.domain.entities.UserProfile
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import javax.inject.Inject

class UserInfoPreferencesKeyValueStorage @Inject constructor(private val sharedPreferences: SharedPreferences) :
    UserInfoKeyValueStorage {
    private fun putInt(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    private fun putString(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    private fun putFloat(key: String, value: Float) {
        sharedPreferences.edit()
            .putFloat(key, value)
            .apply()
    }

    override fun saveGender(gender: Gender) {
        putString(UserInfoKeyValueStorage.GENDER_EXTRA_KEY, gender.name)
    }

    override fun saveGoal(goalType: GoalType) {
        putString(UserInfoKeyValueStorage.GOAL_TYPE_EXTRA_KEY, goalType.name)
    }

    override fun savePhysicalActivity(physicalActivityLevel: PhysicalActivityLevel) {
        putString(
            UserInfoKeyValueStorage.PHYSICAL_ACTIVITY_LEVEL_EXTRA_KEY,
            physicalActivityLevel.name
        )
    }

    override fun saveAge(value: Int) {
        putInt(UserInfoKeyValueStorage.AGE_EXTRA_KEY, value)
    }

    override fun saveHeight(value: Int) {
        putInt(UserInfoKeyValueStorage.HEIGHT_EXTRA_KEY, value)
    }

    override fun saveWeight(value: Float) {
        putFloat(UserInfoKeyValueStorage.WEIGHT_EXTRA_KEY, value)
    }

    override fun saveCarbRatio(value: Float) {
        putFloat(UserInfoKeyValueStorage.CARB_RATION_EXTRA_KEY, value)
    }

    override fun saveProteinRatio(value: Float) {
        putFloat(UserInfoKeyValueStorage.PROTEIN_RATION_EXTRA_KEY, value)
    }

    override fun saveFatRatio(value: Float) {
        putFloat(UserInfoKeyValueStorage.FAT_RATION_EXTRA_KEY, value)
    }

    override fun readAllUserInfo(): UserProfile {
        val gender = Gender.parse(
            sharedPreferences.getString(
                UserInfoKeyValueStorage.GENDER_EXTRA_KEY,
                ""
            )!!
        )
        val goalType = GoalType.parse(
            sharedPreferences.getString(
                UserInfoKeyValueStorage.GOAL_TYPE_EXTRA_KEY,
                ""
            )!!
        )
        val activityLevel = PhysicalActivityLevel.parse(
            sharedPreferences.getString(
                UserInfoKeyValueStorage.PHYSICAL_ACTIVITY_LEVEL_EXTRA_KEY,
                ""
            )!!
        )
        val age = sharedPreferences.getInt(UserInfoKeyValueStorage.AGE_EXTRA_KEY, -1)
        val height = sharedPreferences.getInt(UserInfoKeyValueStorage.HEIGHT_EXTRA_KEY, -1)
        val weight = sharedPreferences.getFloat(UserInfoKeyValueStorage.WEIGHT_EXTRA_KEY, -1f)
        val carbRatio =
            sharedPreferences.getFloat(UserInfoKeyValueStorage.CARB_RATION_EXTRA_KEY, -1f)
        val proteinRatio =
            sharedPreferences.getFloat(UserInfoKeyValueStorage.PROTEIN_RATION_EXTRA_KEY, -1f)
        val fatRatio = sharedPreferences.getFloat(UserInfoKeyValueStorage.FAT_RATION_EXTRA_KEY, -1f)

        return UserProfile(
            gender = gender,
            goalType = goalType,
            physicalActivityLevel = activityLevel,
            age = age,
            weight = weight,
            height = height,
            fatRatio = fatRatio,
            carbRatio = carbRatio,
            proteinRatio = proteinRatio
        )
    }
}