package com.voitov.tracker_data.mapper

import com.voitov.tracker_data.local.entity.TrackedFoodEntity
import com.voitov.tracker_domain.model.FoodStatistics
import java.time.LocalDate

fun TrackedFoodEntity.toFoodStatistics() =
    FoodStatistics(
        date = LocalDate.of(year, month, dayOfMonth),
        fats = fat,
        carbs = carbs,
        proteins = proteins
    )