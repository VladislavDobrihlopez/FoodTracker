package com.voitov.tracker_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voitov.common_ui.LocalSpacing

@Composable
fun NutrientValueInfoIndicator(
    amount: String,
    unit: String,
    modifier: Modifier = Modifier,
    amountTextSize: TextUnit = 20.sp,
    amountColor: Color = MaterialTheme.colors.onBackground,
    unitTextSize: TextUnit = 12.sp,
    unitColor: Color = MaterialTheme.colors.onBackground,
    nutrientColor: Color = MaterialTheme.colors.onBackground,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(nutrientColor))
        Spacer(Modifier.width(spacing.spaceExtraSmall))
        UiNumberFollowedByUnit(
            amount = amount,
            unit = unit,
            amountTextSize = amountTextSize,
            amountColor = amountColor,
            unitTextSize = unitTextSize,
            unitColor = unitColor
        )
    }
}