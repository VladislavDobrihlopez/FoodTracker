package com.voitov.tracker_domain.model

import java.time.LocalDate

data class FoodStatistics(
    val date: LocalDate,
    val inTotalKkal: Int = 0,
    val fats: Int,
    val carbs: Int,
    val proteins: Int
)
