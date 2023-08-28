package com.voitov.tracker_presentation.searching_for_food_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voitov.common_ui.LocalSpacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchConfigChip(
    isSelected: Boolean,
    unSelectedText: String,
    selectedText: String,
    @DrawableRes imageResId: Int,
    textStyle: TextStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageSize: Dp = 24.dp,
    unselectedBackgroundColor: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.25f),
    selectedBackgroundColor: Color = MaterialTheme.colors.primary
) {
    val spacing = LocalSpacing.current

    Chip(
        border = BorderStroke(2.dp, if (isSelected) selectedBackgroundColor else unselectedBackgroundColor),
        onClick = onClick,
        modifier = modifier.height(48.dp)
    ) {
        Image(
            modifier = Modifier.height(imageSize),
            painter = painterResource(id = imageResId),
            contentDescription = selectedText
        )
        Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
        Text(
            text = if (isSelected) {
                selectedText
            } else {
                unSelectedText
            },
            style = textStyle,
            color = MaterialTheme.colors.onSurface
        )
    }
}