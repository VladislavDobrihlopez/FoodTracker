package com.voitov.tracker_presentation.trackable_food_manager_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.voitov.common_ui.LocalSpacing

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ActionSection(
    isWrapped: Boolean,
    sectionDescription: String,
    actionText: String,
    onCardClick: () -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    clipRadius: CornerBasedShape = MaterialTheme.shapes.medium,
) {
    val spacing = LocalSpacing.current
    Card(
        modifier = modifier
            .clip(clipRadius)
            .background(MaterialTheme.colors.primaryVariant)
            .clickable { onCardClick() },
        elevation = 4.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    BorderStroke(2.dp, MaterialTheme.colors.primaryVariant),
                    clipRadius
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = isWrapped,
                label = "wrap_anim"
            ) { wrappedState ->
                if (wrappedState) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(spacing.spaceMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.alpha(1f),
                            text = sectionDescription,
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(spacing.spaceMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            modifier = Modifier.alpha(1f),
                            text = actionText,
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.onSurface
                        )
                        OutlinedButton(
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onActionClick
                        ) {
                            Text(
                                text = "Go",
                                style = MaterialTheme.typography.button,
                            )
                        }
                    }
                }
            }
        }
    }
}
