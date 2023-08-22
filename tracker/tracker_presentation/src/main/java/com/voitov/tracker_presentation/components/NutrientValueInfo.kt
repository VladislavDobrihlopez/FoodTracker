package com.voitov.tracker_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.voitov.common_ui.LocalSpacing

@Composable
fun NutrientValueInfo(
    nutrientName: String,
    amount: String,
    unit: String,
    modifier: Modifier = Modifier,
    amountTextSize: TextUnit = 20.sp,
    amountColor: Color = MaterialTheme.colors.onBackground,
    unitTextSize: TextUnit = 12.sp,
    unitColor: Color = MaterialTheme.colors.onBackground,
    nutrientColor: Color = MaterialTheme.colors.onBackground,
    nutrientTextStyle: TextStyle = MaterialTheme.typography.body1,
) {
    val spacing = LocalSpacing.current
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        UiNumberFollowedByUnit(
            amount = amount,
            unit = unit,
            amountTextSize = amountTextSize,
            amountColor = amountColor,
            unitTextSize = unitTextSize,
            unitColor = unitColor
        )
        Spacer(Modifier.height(spacing.spaceExtraSmall))
        Text(text = nutrientName, color = nutrientColor, style = nutrientTextStyle)
    }
}