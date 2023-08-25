package com.voitov.onboarding_presentation.goal_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.domain.entities.GoalType
import com.voitov.common_ui.LocalSpacing
import com.voitov.common.utils.UiEvents
import com.voitov.onboarding_presentation.components.ActionButton
import com.voitov.onboarding_presentation.components.SelectionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun GoalScreen(
    onNavigate: () -> Unit,
    viewModel: GoalViewModel = hiltViewModel<GoalViewModel>(),
) {
    val spacing = LocalSpacing.current
    val scope = remember { CoroutineScope(Dispatchers.Main.immediate) }
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent
            .onEach { event ->
                Log.d("TEST_CHANNEL", "delivered")
                when (event) {
                    UiEvents.DispatchNavigationRequest -> onNavigate()
                    else -> throw IllegalStateException()
                }
            }
            .launchIn(scope)
    }

    Box(
        modifier = Modifier
            .padding(spacing.spaceMedium)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.lose_keep_or_gain_weight),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3
            )
            Spacer(Modifier.height(spacing.spaceMedium))

            Row(modifier = Modifier.padding(spacing.spaceSmall)) {
                val currentGoalType = viewModel.goalState

                SelectionButton(
                    value = stringResource(id = R.string.lose),
                    onButtonClick = { viewModel.onSelect(GoalType.LoseWeight) },
                    isSelected = currentGoalType is GoalType.LoseWeight,
                    selectedTextColor = MaterialTheme.colors.onPrimary
                )

                Spacer(Modifier.width(spacing.spaceSmall))

                SelectionButton(
                    value = stringResource(id = R.string.keep),
                    onButtonClick = { viewModel.onSelect(GoalType.KeepWeight) },
                    isSelected = currentGoalType is GoalType.KeepWeight,
                    selectedTextColor = MaterialTheme.colors.onPrimary
                )

                Spacer(Modifier.width(spacing.spaceSmall))

                SelectionButton(
                    value = stringResource(id = R.string.gain),
                    onButtonClick = { viewModel.onSelect(GoalType.GainWeight) },
                    isSelected = currentGoalType is GoalType.GainWeight,
                    selectedTextColor = MaterialTheme.colors.onPrimary
                )
            }
        }

        ActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            text = stringResource(id = R.string.next)
        ) {
            viewModel.onNavigate()
        }
    }
}
