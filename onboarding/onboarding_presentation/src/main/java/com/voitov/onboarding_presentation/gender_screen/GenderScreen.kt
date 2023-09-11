package com.voitov.onboarding_presentation.gender_screen

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
import com.voitov.common.domain.entities.Gender
import com.voitov.common.utils.UiSideEffect
import com.voitov.common_ui.LocalSpacing
import com.voitov.onboarding_presentation.components.ActionButton
import com.voitov.onboarding_presentation.components.SelectionButton

@Composable
fun GenderScreen(
    onNavigate: () -> Unit,
    viewModel: GenderViewModel = hiltViewModel(),
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
                text = stringResource(id = R.string.whats_your_gender),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3
            )
            Spacer(Modifier.height(spacing.spaceMedium))

            Row(modifier = Modifier.padding(spacing.spaceSmall)) {
                val currentGender = viewModel.genderState

                SelectionButton(
                    value = stringResource(id = R.string.male),
                    onButtonClick = { viewModel.onSelect(Gender.Male) },
                    isSelected = currentGender is Gender.Male,
                    selectedTextColor = MaterialTheme.colors.onPrimary
                )

                Spacer(Modifier.width(spacing.spaceMedium))

                SelectionButton(
                    value = stringResource(id = R.string.female),
                    onButtonClick = { viewModel.onSelect(Gender.Female) },
                    isSelected = currentGender is Gender.Female,
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
