package com.voitov.onboarding_presentation.nutrient_plan_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.utils.UiSideEffect
import com.voitov.common_ui.LocalSpacing
import com.voitov.onboarding_presentation.components.ActionButton
import com.voitov.onboarding_presentation.components.UnitEditText

@Composable
fun NutrientPlanScreen(
    snackBarState: SnackbarHostState,
    onNavigate: () -> Unit,
    viewModel: NutrientPlanViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiSideEffect.DispatchNavigationRequest -> onNavigate()
                is UiSideEffect.ShowUpSnackBar -> {
                    snackBarState.showSnackbar(message = event.text.asString(context))
                }

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
                text = stringResource(id = R.string.what_are_your_nutrient_goals),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3
            )
            Spacer(Modifier.height(spacing.spaceMedium))

            val screenState = viewModel.screenState

            UnitEditText(
                value = screenState.carbRatio,
                unit = stringResource(id = R.string.percent_carbs),
                onValueChange = {
                    viewModel.onEvent(NutrientScreenEvent.OnValueEnter.OnCarbRatioEnter(it))
                })

            UnitEditText(
                value = screenState.fatRatio,
                unit = stringResource(id = R.string.percent_fats),
                onValueChange = {
                    viewModel.onEvent(NutrientScreenEvent.OnValueEnter.OnFatRatioEnter(it))
                })

            UnitEditText(
                value = screenState.proteinRation,
                unit = stringResource(id = R.string.percent_proteins),
                onValueChange = {
                    viewModel.onEvent(NutrientScreenEvent.OnValueEnter.OnProteinsRatioEnter(it))
                })
        }

        ActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            text = stringResource(id = R.string.next)
        ) {
            viewModel.onEvent(NutrientScreenEvent.OnClickNavigationElement)
        }
    }
}