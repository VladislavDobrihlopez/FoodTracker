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
import com.voitov.common_ui.LocalSpacing

@Composable
fun NutrientValueInfo(
    nutrientName: String,
    amount: String,
    unit: String,
    modifier: Modifier = Modifier,
    nutrientColor: Color = MaterialTheme.colors.onBackground,
    nutrientTextStyle: TextStyle = MaterialTheme.typography.body1,
) {
    val spacing = LocalSpacing.current
    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        UiNumberFollowedByUnit(amount = amount, unit = unit)
        Spacer(Modifier.height(spacing.spaceExtraSmall))
        Text(text = nutrientName, color = nutrientColor, style = nutrientTextStyle)
    }
}