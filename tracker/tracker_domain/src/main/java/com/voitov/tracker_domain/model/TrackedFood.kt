package com.voitov.tracker_domain.model

import java.time.LocalDateTime

data class TrackedFood(
    val id: Int = 0,
    val name: String,
    val imageUrl: String?,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDateTime,
)
