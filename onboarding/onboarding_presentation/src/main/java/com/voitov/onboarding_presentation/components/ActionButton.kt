package com.voitov.onboarding_presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.voitov.common_ui.LocalSpacing

@Composable
fun ActionButton(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.button,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = isEnabled,
        shape = CircleShape
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.onPrimary,
            modifier = modifier.padding(LocalSpacing.current.spaceMedium),
            style = textStyle
        )
    }
}