package com.voitov.tracker_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("trackable_food")
data class TrackableFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageUri: String?,
    val caloriesPer100g: Int,
    val carbsPer100g: Int,
    val proteinPer100g: Int,
    val fatProteinPer100g: Int,
    val minutes: Int,
    val hours: Int,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int,
)