package com.voitov.tracker_domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackableFood(
    val id: String,
    val name: String,
    val imageSourcePath: String?,
    val caloriesPer100g: Int,
    val carbsPer100g: Int,
    val proteinPer100g: Int,
    val fatProteinPer100g: Int
): Parcelable