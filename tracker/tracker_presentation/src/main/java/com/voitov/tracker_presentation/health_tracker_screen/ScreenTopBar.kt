package com.voitov.tracker_presentation.health_tracker_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ScreenTopBar(
    isExpanded: State<Boolean>,
    doReonboarding: () -> Unit,
    viewExplanations: () -> Unit,
    shouldExpand: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimary,
) {
    Row {
        IconButton(onClick = { shouldExpand(!isExpanded.value) }) {
            Icon(
                if (isExpanded.value) Icons.Default.Close else Icons.Default.KeyboardArrowRight,
                tint = color,
                contentDescription = "close",
            )
        }
        AnimatedVisibility(visible = isExpanded.value) {
            TopBarContent(
                modifier = modifier,
                doReonboarding = doReonboarding,
                viewExplanations = viewExplanations,
                color = color
            )
        }
    }
}

@Composable
private fun TopBarContent(
    doReonboarding: () -> Unit,
    viewExplanations: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        IconButton(onClick = doReonboarding) {
            Icon(Icons.Default.Edit, contentDescription = "do re-onboarding", tint = color)
        }
        IconButton(onClick = viewExplanations) {
            Icon(Icons.Default.Info, contentDescription = "watch explanations", tint = color)
        }
    }
}