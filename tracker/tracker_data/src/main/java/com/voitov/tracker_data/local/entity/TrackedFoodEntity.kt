package com.voitov.tracker_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("food_item")
data class TrackedFoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val carbs: Int,
    val proteins: Int,
    val fat: Int,
    val calories: Int,
    val imageUrl: String?,
    val mealType: String,
    val amount: Int,
    val minutes: Int,
    val hours: Int,
    val dayOfMonth: Int,
    val month: Int,
    val year: Int,
)