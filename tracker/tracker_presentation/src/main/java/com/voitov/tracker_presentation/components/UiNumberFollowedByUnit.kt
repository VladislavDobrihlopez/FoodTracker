package com.voitov.tracker_presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.voitov.common_ui.LocalSpacing

@Composable
fun UiNumberFollowedByUnit(
    amount: String,
    unit: String,
    modifier: Modifier = Modifier,
    amountTextSize: TextUnit = 20.sp,
    amountColor: Color = MaterialTheme.colors.onBackground,
    unitTextSize: TextUnit = 12.sp,
    unitColor: Color = MaterialTheme.colors.onBackground,
) {
    val spacing = LocalSpacing.current
    Row(modifier = modifier) {
        Text(
            modifier = Modifier.alignBy(LastBaseline),
            style = MaterialTheme.typography.body1,
            text = amount,
            color = amountColor,
            fontSize = amountTextSize
        )
        Spacer(Modifier.width(spacing.spaceSmall))
        Text(
            modifier = Modifier.alignBy(LastBaseline),
            style = MaterialTheme.typography.body1,
            text = unit,
            color = unitColor,
            fontSize = unitTextSize
        )
    }
}