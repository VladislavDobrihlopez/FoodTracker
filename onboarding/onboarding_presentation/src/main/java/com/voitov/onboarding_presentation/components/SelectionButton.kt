package com.voitov.onboarding_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.voitov.common_ui.LocalSpacing

@Composable
fun SelectionButton(
    value: String,
    onButtonClick: () -> Unit,
    isSelected: Boolean,
    selectedTextColor: Color,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.button,
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colors.primary else Color.Transparent)
            .border(2.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(100.dp))
            .clickable { onButtonClick() }
            .padding(LocalSpacing.current.spaceMedium)) {
        Text(
            text = value,
            style = textStyle.copy(color = if (isSelected) selectedTextColor else Color.Unspecified)
        )
    }
}