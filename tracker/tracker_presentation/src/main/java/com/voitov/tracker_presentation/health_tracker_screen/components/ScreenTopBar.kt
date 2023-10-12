package com.voitov.tracker_presentation.health_tracker_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.voitov.common.R

@Composable
fun ScreenTopBar(
    isExpanded: Boolean,
    currentMode: ScreenMode,
    switchMode: (ScreenMode) -> Unit,
    doReonboarding: () -> Unit,
    viewExplanations: () -> Unit,
    shouldExpand: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimary,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { shouldExpand(!isExpanded) }) {
                Icon(
                    if (isExpanded) Icons.Default.Close else Icons.Default.KeyboardArrowRight,
                    tint = color,
                    contentDescription = stringResource(id = R.string.content_description_close),
                )
            }
            AnimatedVisibility(visible = isExpanded) {
                TopBarContent(
                    modifier = modifier,
                    doReonboarding = doReonboarding,
                    viewExplanations = viewExplanations,
                    color = color
                )
            }
        }
        IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = {
            switchMode(
                if (currentMode == ScreenMode.HOME) {
                    ScreenMode.CHART
                } else {
                    ScreenMode.HOME
                }
            )
        }) {
            Icon(
                if (currentMode == ScreenMode.HOME) {
                    Icons.Default.Home
                } else {
                    Icons.Default.DateRange
                },
                tint = color,
                contentDescription = stringResource(id = R.string.content_description_close),
            )
        }
    }
}

enum class ScreenMode {
    CHART,
    HOME
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
            Icon(
                Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.content_description_do_reonboarding),
                tint = color
            )
        }
        IconButton(onClick = viewExplanations) {
            Icon(
                Icons.Default.Info,
                contentDescription = stringResource(id = R.string.content_description_take_look_at_explanations),
                tint = color
            )
        }
    }
}