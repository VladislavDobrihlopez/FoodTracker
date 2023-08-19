package com.voitov.onboarding_presentation.welcome.gender_screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.domain.entities.Gender
import com.voitov.common_ui.LocalSpacing
import com.voitov.common_ui.navigation.UiEvents
import com.voitov.onboarding_presentation.welcome.components.ActionButton
import com.voitov.onboarding_presentation.welcome.components.SelectionButton

@Composable
fun GenderScreen(
    viewModel: GenderViewModel = hiltViewModel<GenderViewModel>(),
    onNavigate: () -> Unit,
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvents.DispatchNavigationRequest -> onNavigate()
                else -> throw IllegalStateException()
            }
        }
    }

    Box(modifier = Modifier.padding(spacing.spaceMedium).fillMaxSize(), contentAlignment = Alignment.Center) {
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
                    onButtonClick = { viewModel.onGenderClick(Gender.Male) },
                    isSelected = currentGender is Gender.Male,
                    selectedTextColor = MaterialTheme.colors.onPrimary
                )

                Spacer(Modifier.width(spacing.spaceMedium))

                SelectionButton(
                    value = stringResource(id = R.string.female),
                    onButtonClick = { viewModel.onGenderClick(Gender.Female) },
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

@Preview
@Composable
internal fun PreviewGenderScreen() {
    GenderScreen {
    }
}