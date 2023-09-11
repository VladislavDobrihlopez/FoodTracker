package com.voitov.onboarding_presentation.activity_level_screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.domain.entities.PhysicalActivityLevel
import com.voitov.common.utils.UiSideEffect
import com.voitov.common_ui.LocalSpacing
import com.voitov.onboarding_presentation.components.ActionButton
import com.voitov.onboarding_presentation.components.SelectionButton

@Composable
fun ActivityLevelScreen(
    onNavigate: () -> Unit,
    viewModel: ActivityLevelViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiSideEffect.DispatchNavigationRequest -> onNavigate()
                else -> throw IllegalStateException()
            }
        }
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
                text = stringResource(id = R.string.whats_your_activity_level),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3
            )
            Spacer(Modifier.height(spacing.spaceMedium))

            Row(modifier = Modifier.padding(spacing.spaceSmall)) {
                val currentPhysicalActivityLevel = viewModel.activityLevelState

                SelectionButton(
                    value = stringResource(id = R.string.low),
                    onButtonClick = { viewModel.onSelect(PhysicalActivityLevel.Low) },
                    isSelected = currentPhysicalActivityLevel is PhysicalActivityLevel.Low,
                    selectedTextColor = MaterialTheme.colors.onPrimary
                )

                Spacer(Modifier.width(spacing.spaceSmall))

                SelectionButton(
                    value = stringResource(id = R.string.medium),
                    onButtonClick = { viewModel.onSelect(PhysicalActivityLevel.Medium) },
                    isSelected = currentPhysicalActivityLevel is PhysicalActivityLevel.Medium,
                    selectedTextColor = MaterialTheme.colors.onPrimary
                )

                Spacer(Modifier.width(spacing.spaceSmall))

                SelectionButton(
                    value = stringResource(id = R.string.high),
                    onButtonClick = { viewModel.onSelect(PhysicalActivityLevel.High) },
                    isSelected = currentPhysicalActivityLevel is PhysicalActivityLevel.High,
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
