package com.voitov.tracker_domain.model

import java.time.LocalDateTime

data class  TrackedFood(
    val id: Int = 0,
    val name: String,
    val imageUrl: String?,
    val calories: Int,
    val carbs: Int,
    val proteins: Int,
    val fats: Int,
    val mealTimeType: MealTimeType,
    val amount: Int,
    val date: LocalDateTime,
)
