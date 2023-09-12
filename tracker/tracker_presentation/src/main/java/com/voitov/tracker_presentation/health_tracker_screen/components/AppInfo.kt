package com.voitov.tracker_presentation.health_tracker_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.voitov.common.Configuration
import com.voitov.common.R
import com.voitov.common_ui.LocalSpacing

@Composable
fun AppInfo(
    isShownState: State<Boolean>,
    onDismissClick: () -> Unit,
    onOkayClick: () -> Unit,
    modifier: Modifier = Modifier,
    descriptionTextSize: TextUnit = 18.sp
) {
    val spacing = LocalSpacing.current
    AnimatedVisibility(visible = isShownState.value) {
        Dialog(onDismissRequest = {
            onDismissClick()
        }) {
            Column(
                modifier = modifier
                    .clip(MaterialTheme.shapes.large)
                    .padding(2.dp)
                    .shadow(elevation = 4.dp, MaterialTheme.shapes.large)
                    .background(MaterialTheme.colors.surface)
                    .padding(spacing.spaceMedium),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    modifier = Modifier
                        .size(144.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(
                                2.dp,
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF9575CD),
                                        Color(0xFFBA68C8),
                                        Color(0xFFE57373),
                                        Color(0xFFFFB74D),
                                        Color(0xFFFFF176),
                                        Color(0xFFAED581),
                                        Color(0xFF4DD0E1),
                                        Color(0xFF9575CD)
                                    )
                                )
                            ),
                            CircleShape
                        ),
                    painter = painterResource(id = R.drawable.author_photo),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.developed_by),
                    style = MaterialTheme.typography.body1,
                    fontSize = descriptionTextSize,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Text(
                    text = stringResource(R.string.contacts),
                    style = MaterialTheme.typography.body1,
                    fontSize = descriptionTextSize,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    text = stringResource(id = R.string.app_version, Configuration.APP_VERSION),
                    style = MaterialTheme.typography.body1,
                    fontSize = descriptionTextSize,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                TextButton(modifier = Modifier.align(Alignment.End), onClick = onOkayClick) {
                    Text(
                        text = stringResource(id = R.string.okay),
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}