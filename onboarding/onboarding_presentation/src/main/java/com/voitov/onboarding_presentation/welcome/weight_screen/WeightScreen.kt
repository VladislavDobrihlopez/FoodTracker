package com.voitov.onboarding_presentation.welcome.weight_screen

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common_ui.LocalSpacing
import com.voitov.common_ui.UiEvents
import com.voitov.onboarding_presentation.welcome.components.ActionButton
import com.voitov.onboarding_presentation.welcome.components.UnitEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun WeightScreen(
    snackBarState: SnackbarHostState,
    onNavigate: () -> Unit,
    viewModel: WeightViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val scope = remember { CoroutineScope(Dispatchers.Main.immediate) }
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent
            .onEach { event ->
                Log.d("TEST_CHANNEL", "delivered")
                when (event) {
                    UiEvents.DispatchNavigationRequest -> onNavigate()
                    is UiEvents.ShowUpSnackBar -> {
                        snackBarState.showSnackbar(message = event.text.asString(context))
                    }

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
                text = stringResource(id = R.string.whats_your_weight),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h3
            )
            Spacer(Modifier.height(spacing.spaceMedium))

            val currentHeight = viewModel.weightState
            UnitEditText(
                value = currentHeight,
                unit = stringResource(id = R.string.kg),
                onValueChange = {
                    viewModel.onChange(it)
                })
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