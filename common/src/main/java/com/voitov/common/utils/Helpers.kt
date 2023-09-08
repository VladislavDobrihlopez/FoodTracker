package com.voitov.common.utils

import com.voitov.common.Configuration
import kotlin.math.roundToInt

fun areNutrientComponentsCorrect(
    calories: Int,
    carbohydrates: Int,
    fat: Int,
    protein: Int,
    lowerBoundCoefficient: Float,
    upperBoundCoefficient: Float
): Boolean {
    val calculatedCalories = calculateCalories(carbohydrates, fat, protein)
    return calculatedCalories in ((calories * lowerBoundCoefficient).roundToInt()..(calculatedCalories * upperBoundCoefficient).roundToInt())
}

fun calculateCalories(carbohydrates: Int, fat: Int, protein: Int): Int {
    return fat * Configuration.CALORIES_PER_FAT_GRAM + carbohydrates * Configuration.CALORIES_PER_CARBOHYDRATES_GRAM + protein * Configuration.CALORIES_PER_PROTEIN_GRAM
}