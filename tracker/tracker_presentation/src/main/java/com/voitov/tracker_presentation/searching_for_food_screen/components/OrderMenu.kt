package com.voitov.tracker_presentation.searching_for_food_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.voitov.common_ui.LocalSpacing
import com.voitov.common.R
import com.voitov.tracker_domain.model.MealPhysicsType
import com.voitov.tracker_presentation.searching_for_food_screen.model.OrderMenuUiModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun OrderMenu(
    state: OrderMenuUiModel,
    modifier: Modifier = Modifier,
    onMealTypeClick: (MealPhysicsType) -> Unit,
    onBadgeCountClick: (MealPhysicsType) -> Unit,
    actions: @Composable (RowScope.() -> Unit)? = null,
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .padding(spacing.spaceExtraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.liquids.forEach { cup ->
                    BadgedBox(badge = {
                        Badge(modifier = Modifier.clickable { onBadgeCountClick(cup.first) }) {
                            Text(text = cup.second.toString())
                        }
                    }) {
                        Chip(border = BorderStroke(2.dp, MaterialTheme.colors.primary), onClick = {
                            onMealTypeClick(cup.first)
                        }) {
                            Image(imageVector = ImageVector.vectorResource(id = R.drawable.cup), contentDescription = "")
                            Text(text = "${cup.first.inGrams}")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.solids.forEach { solid ->
                    BadgedBox(badge = {
                        Badge(modifier = Modifier.clickable { onBadgeCountClick(solid.first) }) {
                            Text(text = solid.second.toString())
                        }
                    }) {
                        Chip(border = BorderStroke(2.dp, MaterialTheme.colors.primary), onClick = {
                            onMealTypeClick(solid.first)
                        }) {
                            Image(imageVector = ImageVector.vectorResource(id = R.drawable.hamburger), contentDescription = "")
                            Text(text = "${solid.first.inGrams}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
        actions?.invoke(this)
    }
}