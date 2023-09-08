package com.voitov.tracker_presentation.trackable_food_manager_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.nav.TrackableFoodManagerSection
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_presentation.trackable_food_manager_screen.components.ActionSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun TrackableFoodManagerScreen(
    onNavigate: (TrackableFoodManagerSection) -> Unit,
    viewModel: TrackableFoodManagerViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val scope = remember {
        CoroutineScope(Dispatchers.Main.immediate)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent
            .onEach { event ->
                when(event) {
                    is TrackableFoodManagerUiEvent.NavigateToSection -> {
                        onNavigate(event.section)
                    }
                }
            }
            .launchIn(scope)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceSmall)
    ) {
        Text(text = "Options", style = MaterialTheme.typography.h1)
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        LazyHorizontalGrid(
            rows = GridCells.Adaptive(192.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(spacing.spaceSmall),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            items(viewModel.screenState.sections) { sectionCard ->
                ActionSection(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(spacing.spaceSmall),
                    isWrapped = sectionCard.isInWrappedState,
                    sectionDescription = sectionCard.description.asString(context),
                    actionText = sectionCard.action.asString(context),
                    onCardClick = {
                        viewModel.onEvent(
                            TrackableFoodManagerScreenEvent.OnCardContentClick(
                                sectionCard
                            )
                        )
                    },
                    onActionClick = {
                        viewModel.onEvent(
                            TrackableFoodManagerScreenEvent.OnNavAgreementButtonClick(
                                sectionCard
                            )
                        )
                    }
                )
            }
        }
    }
}