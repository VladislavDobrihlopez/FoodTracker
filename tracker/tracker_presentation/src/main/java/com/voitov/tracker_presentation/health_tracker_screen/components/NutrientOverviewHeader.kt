package com.voitov.tracker_presentation.health_tracker_screen.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voitov.common.R
import com.voitov.common_ui.CarbColor
import com.voitov.common_ui.FatColor
import com.voitov.common_ui.LocalSpacing
import com.voitov.common_ui.ProteinColor
import com.voitov.tracker_presentation.components.EatenFoodCircularBar
import com.voitov.tracker_presentation.components.EatenFoodOverviewHorizontalBar
import com.voitov.tracker_presentation.components.UiNumberFollowedByUnit
import com.voitov.tracker_presentation.health_tracker_screen.HealthTrackerScreenState

@Composable
fun NutrientOverviewHeader(
    isTopBarExpanded: State<Boolean>,
    onToggleTopBar: () -> Unit,
    onAppInfoClick: () -> Unit,
    onDoReonboardingClick: () -> Unit,
    state: HealthTrackerScreenState,
) {
    val spacing = LocalSpacing.current
    val animatedCaloriesHorizontalBar = animateIntAsState(
        targetValue = state.caloriesPerDayInFact
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 45.dp, bottomEnd = 45.dp))
            .background(MaterialTheme.colors.primary)
    ) {
        ScreenTopBar(
            isExpanded = isTopBarExpanded,
            doReonboarding = onDoReonboardingClick,
            viewExplanations = onAppInfoClick,
            shouldExpand = {
                onToggleTopBar()
            }
        )

        Column(
            modifier = Modifier.padding(
                horizontal = spacing.spaceLarge,
                vertical = spacing.spaceLarge
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UiNumberFollowedByUnit(
                    amount = animatedCaloriesHorizontalBar.value.toString(),
                    amountTextSize = 40.sp,
                    unitTextSize = 24.sp,
                    amountColor = MaterialTheme.colors.onPrimary,
                    unitColor = MaterialTheme.colors.onPrimary,
                    unit = stringResource(id = R.string.kcal),
                    modifier = Modifier.align(Alignment.Bottom)
                )
                Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                Text(
                    text = "/",
                    color = MaterialTheme.colors.onPrimary,
                    fontSize = 40.sp,
                    modifier = Modifier.align(Alignment.Bottom)
                )
                Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))
                Column {
                    Text(
                        text = stringResource(id = R.string.your_goal),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onPrimary
                    )
                    UiNumberFollowedByUnit(
                        amount = state.caloriesPerDayGoal.toString(),
                        amountTextSize = 40.sp,
                        unitTextSize = 24.sp,
                        amountColor = MaterialTheme.colors.onPrimary,
                        unitColor = MaterialTheme.colors.onPrimary,
                        unit = stringResource(id = R.string.kcal)
                    )
                }
            }
            Spacer(Modifier.height(spacing.spaceSmall))
            EatenFoodOverviewHorizontalBar(
                calories = state.caloriesPerDayInFact,
                caloriesGoal = state.caloriesPerDayGoal,
                fat = state.fatPerDayInFact,
                carbs = state.carbsPerDayInFact,
                proteins = state.proteinsPerDayInFact,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EatenFoodCircularBar(
                    value = state.carbsPerDayInFact,
                    goal = state.carbsPerDayGoal,
                    name = stringResource(id = R.string.carbs),
                    color = CarbColor,
                    modifier = Modifier.size(90.dp)
                )
                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                EatenFoodCircularBar(
                    value = state.fatPerDayInFact,
                    goal = state.fatPerDayGoal,
                    name = stringResource(id = R.string.fat),
                    color = FatColor,
                    modifier = Modifier.size(90.dp)
                )
                Spacer(modifier = Modifier.width(spacing.spaceSmall))
                EatenFoodCircularBar(
                    value = state.proteinsPerDayInFact,
                    goal = state.proteinsPerDayGoal,
                    name = stringResource(id = R.string.protein),
                    color = ProteinColor,
                    modifier = Modifier.size(90.dp)
                )
            }
        }
    }
}