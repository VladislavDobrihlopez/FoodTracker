package com.voitov.tracker_data.mapper

import com.voitov.tracker_data.local.entity.TrackedFoodEntity
import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_domain.model.TrackedFood
import java.time.LocalDateTime

fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        id = this.id,
        name = this.name,
        carbs = this.carbs,
        proteins = this.protein,
        fat = this.fat,
        calories = this.calories,
        imageUrl = this.imageUrl,
        mealType = this.mealType.name,
        amount = this.amount,
        minutes = this.date.minute,
        hours = this.date.hour,
        dayOfMonth = this.date.dayOfMonth,
        month = this.date.monthValue,
        year = this.date.year,
    )
}

fun TrackedFoodEntity.toTrackedFood(): TrackedFood {
    return TrackedFood(
        id = this.id,
        name = this.name,
        carbs = this.carbs,
        protein = this.proteins,
        fat = this.fat,
        calories = this.calories,
        imageUrl = this.imageUrl,
        mealType = MealType.valueOf(this.mealType),
        amount = this.amount,
        date = LocalDateTime.of(this.year, this.month, this.dayOfMonth, this.hours, this.minutes)
    )
}