package com.voitov.tracker_presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.voitov.common.R
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_presentation.utils.formatMealDate

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoilApi::class, ExperimentalLayoutApi::class)
@Composable
fun TrackedFoodItem(
    item: TrackedFood,
    keepAlive: State<Boolean>,
    modifier: Modifier = Modifier,
    onDeleteItem: () -> Unit
) {
    val spacing = LocalSpacing.current

    val time = remember(item.date) {
        formatMealDate(item.date)
    }

    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToStart) {
            onDeleteItem()
            true
        } else {
            false
        }
    })

    LaunchedEffect(key1 = keepAlive.value) {
        if (keepAlive.value) {
            dismissState.reset()
        }
    }

    if (!dismissState.isDismissed(DismissDirection.EndToStart) || keepAlive.value) {
        SwipeToDismiss(
            modifier = Modifier,
            state = dismissState,
            directions = setOf(DismissDirection.EndToStart),
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = spacing.spaceMedium,
                            top = spacing.spaceSmall,
                            bottom = spacing.spaceSmall,
                            end = spacing.spaceSmall
                        )
                        .background(Color.Red)
                )
            }) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(MaterialTheme.colors.surface)
                    .padding(end = spacing.spaceSmall)
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = item.imageUrl,
                        builder = {
                            crossfade(true)
                            error(R.drawable.food_error)
                            fallback(R.drawable.food_error)
                        }
                    ),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                )
                Spacer(Modifier.width(spacing.spaceExtraSmall))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(4f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = time, style = MaterialTheme.typography.body1)
                        Text(
                            text = item.name,
                            maxLines = 2,
                            style = MaterialTheme.typography.body1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row {
                            Text(
                                text = stringResource(
                                    R.string.nutrient_info,
                                    item.amount,
                                    item.calories
                                )
                            )
                        }
                    }
                    Spacer(Modifier.width(spacing.spaceExtraSmall))
                    FlowRow(modifier = Modifier.weight(6f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                        NutrientValueInfo(
                            nutrientName = stringResource(id = R.string.carbs),
                            amount = item.carbs.toString(),
                            unit = stringResource(id = R.string.grams),
                            amountTextSize = 16.sp,
                            unitTextSize = 12.sp,
                            nutrientTextStyle = MaterialTheme.typography.body2
                        )
                        Spacer(Modifier.width(spacing.spaceExtraSmall))
                        NutrientValueInfo(
                            nutrientName = stringResource(id = R.string.fat),
                            amount = item.fats.toString(),
                            unit = stringResource(id = R.string.grams),
                            amountTextSize = 16.sp,
                            unitTextSize = 12.sp,
                            nutrientTextStyle = MaterialTheme.typography.body2
                        )
                        Spacer(Modifier.width(spacing.spaceExtraSmall))
                        NutrientValueInfo(
                            nutrientName = stringResource(id = R.string.protein),
                            amount = item.proteins.toString(),
                            unit = stringResource(id = R.string.grams),
                            amountTextSize = 16.sp,
                            unitTextSize = 12.sp,
                            nutrientTextStyle = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}