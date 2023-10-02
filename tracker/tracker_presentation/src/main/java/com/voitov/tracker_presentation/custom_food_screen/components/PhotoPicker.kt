package com.voitov.tracker_presentation.custom_food_screen.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.voitov.common.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PhotoPicker(
    onContentClick: () -> Unit,
    modifier: Modifier = Modifier,
    photoUri: Uri? = null,
    imageShape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.primary,
    imageSize: Dp = 144.dp,
    iconMargin: Dp = (imageSize * 0.2f),
    iconColor: Color = MaterialTheme.colors.onPrimary
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(imageSize)
            .clip(imageShape)
            .background(backgroundColor)
    ) {
        if (photoUri == null) {
            IconButton(
                modifier = Modifier.size(imageSize),
                onClick = onContentClick
            ) {
                Image(
                    modifier = Modifier.size(imageSize - iconMargin),
                    imageVector = Icons.Default.Add,
                    colorFilter = ColorFilter.tint(iconColor),
                    contentDescription = stringResource(id = R.string.content_description_add_photo)
                )
            }
        } else {
            Image(
                painter = rememberImagePainter(
                    data = photoUri,
                    builder = {
                        crossfade(true)
                        error(R.drawable.food_error)
                    }
                ),
                contentDescription = stringResource(id = R.string.content_description_some_food),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onContentClick() }
            )
        }
    }
}