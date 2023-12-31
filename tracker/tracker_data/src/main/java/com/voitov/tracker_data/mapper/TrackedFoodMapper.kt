package com.voitov.tracker_data.mapper

import com.voitov.tracker_data.local.entity.TrackedFoodEntity
import com.voitov.tracker_domain.model.MealTimeType
import com.voitov.tracker_domain.model.TrackedFood
import java.time.LocalDateTime

fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        id = this.id,
        name = this.name,
        carbs = this.carbs,
        proteins = this.proteins,
        fat = this.fats,
        calories = this.calories,
        imageUrl = this.imageUrl,
        mealType = this.mealTimeType.name,
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
        proteins = this.proteins,
        fats = this.fat,
        calories = this.calories,
        imageUrl = this.imageUrl,
        mealTimeType = MealTimeType.valueOf(this.mealType),
        amount = this.amount,
        date = LocalDateTime.of(this.year, this.month, this.dayOfMonth, this.hours, this.minutes)
    )
}