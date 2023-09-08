package com.voitov.tracker_domain.model

data class TrackableFood(
    val name: String,
    val imageSourcePath: String?,
    val caloriesPer100g: Int,
    val carbsPer100g: Int,
    val proteinPer100g: Int,
    val fatProteinPer100g: Int
)