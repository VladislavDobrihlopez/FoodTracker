package com.voitov.tracker_presentation.searching_for_food_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.voitov.common.R
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_presentation.components.NutrientValueInfo
import com.voitov.tracker_presentation.searching_for_food_screen.model.TrackableFoodUiModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TrackableFoodUi(
    foodUiModel: TrackableFoodUiModel,
    onClick: () -> Unit,
    onAmountChange: (String) -> Unit,
    onCache: () -> Unit,
    modifier: Modifier = Modifier,
    extraActions: (@Composable RowScope.() -> Unit)? = null
) {
    val food = foodUiModel.food
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .shadow(1.dp, RoundedCornerShape(5.dp))
            .background(MaterialTheme.colors.surface)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)),
                painter = rememberImagePainter(
                    data = food.imageSourcePath,
                    builder = {
                        crossfade(true)
                        error(R.drawable.food_error)
                        fallback(R.drawable.food_error)
                    }
                ),
                contentScale = ContentScale.Crop,
                contentDescription = food.name
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = spacing.spaceSmall, end = spacing.spaceSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Text(
                    text = stringResource(
                        id = R.string.kcal_per_100g,
                        food.caloriesPer100g
                    ),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NutrientValueInfo(
                        nutrientName = stringResource(id = R.string.carbs),
                        amount = food.carbsPer100g.toString(),
                        unit = stringResource(id = R.string.grams),
                        amountTextSize = 16.sp,
                        unitTextSize = 12.sp,
                        nutrientTextStyle = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    NutrientValueInfo(
                        nutrientName = stringResource(id = R.string.fat),
                        amount = food.fatProteinPer100g.toString(),
                        unit = stringResource(id = R.string.grams),
                        amountTextSize = 16.sp,
                        unitTextSize = 12.sp,
                        nutrientTextStyle = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    NutrientValueInfo(
                        nutrientName = stringResource(id = R.string.protein),
                        amount = food.proteinPer100g.toString(),
                        unit = stringResource(id = R.string.grams),
                        amountTextSize = 16.sp,
                        unitTextSize = 12.sp,
                        nutrientTextStyle = MaterialTheme.typography.body2
                    )
                }
            }
        }
        AnimatedVisibility(visible = foodUiModel.isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    BasicTextField(
                        value = foodUiModel.amount,
                        onValueChange = onAmountChange,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = if (foodUiModel.amount.isNotBlank()) {
                                ImeAction.Done
                            } else ImeAction.Default
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onCache()
                                defaultKeyboardAction(ImeAction.Done)
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .border(
                                shape = RoundedCornerShape(5.dp),
                                width = 0.5.dp,
                                color = MaterialTheme.colors.onSurface
                            )
                            .alignBy(LastBaseline)
                            .padding(spacing.spaceMedium)
                            .testTag("trackablefood_textfield")
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                    Text(
                        text = stringResource(id = R.string.grams),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.alignBy(LastBaseline)
                    )
                }
                extraActions?.invoke(this@Row)
                IconButton(
                    onClick = onCache,
                    enabled = foodUiModel.amount.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.track)
                    )
                }
            }
        }
    }
}