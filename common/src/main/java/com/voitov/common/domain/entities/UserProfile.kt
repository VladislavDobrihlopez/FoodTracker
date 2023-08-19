package com.voitov.common.domain.entities

data class UserProfile(
    val gender: Gender,
    val goalType: GoalType,
    val physicalActivityLevel: PhysicalActivityLevel,
    val age: Int,
    val weight: Float,
    val height: Int,
    val fatRatio: Float,
    val proteinRatio: Float,
    val carbRatio: Float
)