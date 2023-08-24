package com.voitov.common.utils

import kotlin.math.roundToInt

fun areNutrientComponentsCorrect(
    calories: Int,
    carbohydrates: Int,
    fat: Int,
    protein: Int,
    lowerBoundCoefficient: Float,
    upperBoundCoefficient: Float
): Boolean {
    val calculatedCalories = fat * 9 + carbohydrates * 4 + protein * 4
    return calculatedCalories in ((calories * lowerBoundCoefficient).roundToInt()..(calculatedCalories * upperBoundCoefficient).roundToInt())
}