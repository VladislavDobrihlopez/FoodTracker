package com.voitov.tracker_presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voitov.common.R
import com.voitov.common_ui.CarbColor
import com.voitov.common_ui.FatColor
import com.voitov.common_ui.LocalSpacing
import com.voitov.common_ui.ProteinColor
import com.voitov.tracker_presentation.health_tracker_screen.model.Meal

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MealItem(
    meal: Meal,
    modifier: Modifier = Modifier,
    picSize: Dp = 60.dp
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        MealImage(
            resId = meal.pictureResId,
            contentDescription = meal.mealTimeType.name,
            size = picSize
        )
        Spacer(modifier = Modifier.width(spacing.spaceSmall))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(text = meal.name.asString(context), style = MaterialTheme.typography.h3)
                UiNumberFollowedByUnit(
                    amountTextSize = 24.sp,
                    unitTextSize = 20.sp,
                    amount = meal.calories.toString(),
                    unit = stringResource(id = R.string.kcal)
                )
            }
            Spacer(Modifier.width(spacing.spaceSmall))
            FlowRow {
                NutrientValueInfoIndicator(
                    nutrientColor = CarbColor,
                    amount = meal.carbohydrates.toString(),
                    unit = stringResource(
                        id = R.string.grams
                    )
                )
                Spacer(Modifier.width(spacing.spaceSmall))
                NutrientValueInfoIndicator(
                    nutrientColor = FatColor,
                    amount = meal.fat.toString(),
                    unit = stringResource(
                        id = R.string.grams
                    )
                )
                Spacer(Modifier.width(spacing.spaceSmall))
                NutrientValueInfoIndicator(
                    nutrientColor = ProteinColor,
                    amount = meal.protein.toString(),
                    unit = stringResource(
                        id = R.string.grams
                    )
                )
            }
        }
    }
}

@Composable
private fun MealImage(resId: Int, contentDescription: String = "", size: Dp) {
    Image(
        modifier = Modifier.size(size),
        painter = painterResource(id = resId),
        contentDescription = contentDescription,
    )
}
