package com.voitov.tracker_data.mapper

import com.voitov.tracker_data.local.entity.TrackableFoodEntity
import com.voitov.tracker_data.remote.dto.ProductDto
import com.voitov.tracker_domain.model.CustomTrackableFood
import com.voitov.tracker_domain.model.TrackableFood
import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.roundToInt

fun ProductDto.toTrackableFood() =
    if (this.productName == null) {
        null
    } else {
        TrackableFood(
            name = this.productName,
            imageSourcePath = this.imageFrontThumbUrl,
            caloriesPer100g = this.nutriments.energyKcal100g.roundToInt(),
            carbsPer100g = this.nutriments.carbohydrates100g.roundToInt(),
            proteinPer100g = this.nutriments.proteins100g.roundToInt(),
            fatProteinPer100g = this.nutriments.fat100g.roundToInt(),
            id = UUID.randomUUID().toString()
        )
    }

fun CustomTrackableFood.toTrackableFoodEntity() = TrackableFoodEntity(
    id = if (this.trackableFood.id.isNotEmpty()) this.trackableFood.id.toInt() else 0,
    name = this.trackableFood.name,
    imageUri = this.trackableFood.imageSourcePath,
    caloriesPer100g = this.trackableFood.caloriesPer100g,
    carbsPer100g = this.trackableFood.carbsPer100g,
    proteinPer100g = this.trackableFood.proteinPer100g,
    fatProteinPer100g = this.trackableFood.fatProteinPer100g,
    minutes = this.date.minute,
    hours = this.date.hour,
    dayOfMonth = this.date.dayOfMonth,
    month = this.date.monthValue,
    year = this.date.year,
)

fun TrackableFoodEntity.toCustomTrackableFood() = CustomTrackableFood(
    trackableFood = TrackableFood(
        name = this.name,
        caloriesPer100g = this.caloriesPer100g,
        carbsPer100g = this.carbsPer100g,
        proteinPer100g = this.proteinPer100g,
        fatProteinPer100g = this.fatProteinPer100g,
        imageSourcePath = this.imageUri,
        id = this.id.toString(),
    ),
    date = LocalDateTime.of(this.year, this.month, this.dayOfMonth, this.hours, this.minutes)
)