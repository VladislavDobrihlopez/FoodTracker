package com.voitov.tracker_data.mapper

import com.voitov.tracker_data.remote.dto.ProductDto
import com.voitov.tracker_domain.models.TrackableFood
import kotlin.math.roundToInt

fun ProductDto.toTrackableFood() =
    if (this.productName == null) {
        null
    } else {
        TrackableFood(
            name = this.productName,
            imageUrl = this.imageFrontThumbUrl,
            caloriesPer100g = this.nutriments.energyKcal100g.roundToInt(),
            carbsPer100g = this.nutriments.carbohydrates100g.roundToInt(),
            proteinPer100g = this.nutriments.proteins100g.roundToInt(),
            fatProteinPer100g = this.nutriments.fat100g.roundToInt()
        )
    }