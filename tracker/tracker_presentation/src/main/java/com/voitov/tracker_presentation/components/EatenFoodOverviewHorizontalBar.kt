package com.voitov.tracker_presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.voitov.common_ui.CarbColor
import com.voitov.common_ui.DarkGreen
import com.voitov.common_ui.FatColor
import com.voitov.common_ui.ProteinColor

@Composable
fun EatenFoodOverviewHorizontalBar(
    calories: Int,
    caloriesGoal: Int,
    fat: Int,
    carbs: Int,
    proteins: Int,
    modifier: Modifier = Modifier,
    fatColor: Color = FatColor,
    carbsColor: Color = CarbColor,
    proteinColor: Color = ProteinColor,
    onExceededGoalPlanColor: Color = MaterialTheme.colors.error,
    onCompletedGoalPlanColor: Color = DarkGreen,
) {
    val backgroundColor = MaterialTheme.colors.background

    val animatedFat = animateFloatAsState(targetValue = (fat * 9f) / caloriesGoal, label = "fat")
    val animatedCarbs =
        animateFloatAsState(targetValue = (carbs * 4f) / caloriesGoal, label = "carbs")
    val animatedProteins =
        animateFloatAsState(targetValue = (proteins * 4f) / caloriesGoal, label = "proteins")

    Canvas(modifier = modifier) {
        val carbsWidth = animatedCarbs.value * size.width
        val fatWidth = animatedFat.value * size.width
        val proteinWidth = animatedProteins.value * size.width

        if (calories > caloriesGoal) {
            drawRoundRect(
                color = onExceededGoalPlanColor,
                cornerRadius = CornerRadius(100f),
                size = Size(size.width, size.height)
            )
        } else if (calories == caloriesGoal) {
            drawRoundRect(
                color = onCompletedGoalPlanColor,
                cornerRadius = CornerRadius(100f),
                size = Size(carbsWidth + fatWidth + proteinWidth, size.height)
            )
        } else {
            val carbsSize = Size(carbsWidth, size.height)
            val fatSize = Size(carbsWidth + fatWidth, size.height)
            val proteinSize = Size(carbsWidth + fatWidth + proteinWidth, size.height)
            drawRoundRect(
                color = backgroundColor,
                size = size,
                cornerRadius = CornerRadius(100f)
            )
            drawRoundRect(
                color = proteinColor,
                cornerRadius = CornerRadius(100f),
                size = proteinSize
            )
            drawRoundRect(
                color = fatColor,
                cornerRadius = CornerRadius(100f),
                size = fatSize
            )
            drawRoundRect(
                color = carbsColor,
                cornerRadius = CornerRadius(100f),
                size = carbsSize
            )
        }
    }
}