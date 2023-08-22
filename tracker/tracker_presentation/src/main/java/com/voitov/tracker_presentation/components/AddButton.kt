package com.voitov.tracker_presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voitov.common_ui.LocalSpacing

@Composable
fun AddButton(
    text: String,
    modifier: Modifier = Modifier,
    borderThin: Dp = 2.dp,
    borderColor: Color = MaterialTheme.colors.primary,
    textColor: Color = MaterialTheme.colors.onBackground,
    textStyle: TextStyle = MaterialTheme.typography.button,
    onClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    Row(modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(100f))
        .clickable { onClick() }
        .border(borderThin, borderColor, RoundedCornerShape(100f))
        .padding(spacing.spaceSmall),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = textStyle, color = textColor)
    }
}