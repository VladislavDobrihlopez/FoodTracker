package com.voitov.tracker_presentation.health_tracker_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.utils.UiSideEffect
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_domain.model.MealTimeType
import com.voitov.tracker_presentation.components.AddButton
import com.voitov.tracker_presentation.components.MealItem
import com.voitov.tracker_presentation.components.TrackedFoodItem
import com.voitov.tracker_presentation.health_tracker_screen.components.AppInfo
import com.voitov.tracker_presentation.health_tracker_screen.components.DaySelector
import com.voitov.tracker_presentation.health_tracker_screen.components.NutrientOverviewHeader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HealthTrackerScreen(
    snackbarHostState: SnackbarHostState,
    onDoReonboarding: () -> Unit,
    onNavigate: (MealTimeType, Int, Int, Int) -> Unit,
    viewModel: HealthTrackerOverviewViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val screenState = viewModel.screenState
    val context = LocalContext.current
    val appInfoDialogIsShownState = rememberSaveable {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiSideEffect.ShowUpSnackBar -> {
                    snackbarHostState.showSnackbar(event.text.asString(context))
                }

                else -> throw IllegalStateException()
            }
        }
    }

    AppInfo(
        isShownState = appInfoDialogIsShownState,
        onDismissClick = {
            appInfoDialogIsShownState.value = false
        },
        onOkayClick = {
            appInfoDialogIsShownState.value = false
        }
    )

    val areTopBarActionsExpanded = remember(viewModel.screenState.areTopBarActionsExpanded) {
        mutableStateOf(viewModel.screenState.areTopBarActionsExpanded)
    }

    val dateTime = remember(screenState.dateTime) {
        mutableStateOf(screenState.dateTime)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            NutrientOverviewHeader(
                isTopBarExpanded = areTopBarActionsExpanded,
                state = screenState,
                onAppInfoClick = {
                    appInfoDialogIsShownState.value = true
                },
                onDoReonboardingClick = {
                    viewModel.onEvent(HealthTrackerScreenEvent.DoReonbording)
                    onDoReonboarding()
                },
                onToggleTopBar = {
                    viewModel.onEvent(HealthTrackerScreenEvent.ToggleTopBar)
                }
            )

            DaySelector(
                date = dateTime,
                onPreviousDayClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToPreviousDay) },
                onNextDayClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToNextDay) },
                onPreviousWeekClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToWeekBehind) },
                onNextWeekClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToWeekAhead) }
            )
        }

        items(screenState.mealsDuringCurrentDay) { meal ->
            MealItem(
                modifier = Modifier
                    .padding(
                        horizontal = spacing.spaceSmall,
                        vertical = spacing.spaceExtraSmall
                    )
                    .clickable {
                        viewModel.onEvent(
                            HealthTrackerScreenEvent.ToggleMeal(
                                mealTimeType = meal.mealTimeType
                            )
                        )
                    }, meal = meal
            )
            AnimatedVisibility(
                visible = meal.isExpanded,
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                val filteredItems =
                    screenState.trackedFoods.filter { meal.mealTimeType == it.mealTimeType }.toList()
                LazyColumn(
                    modifier = Modifier.height((100.dp) * (filteredItems.size + 1)),
                    verticalArrangement = Arrangement.spacedBy(spacing.spaceExtraSmall),
                    contentPadding = PaddingValues(spacing.spaceSmall)
                ) {
                    items(filteredItems, key = { it.id }) { foodItem ->
                        val keepAliveState = remember {
                            mutableStateOf<Boolean>(true)
                        }

                        TrackedFoodItem(
                            item = foodItem,
                            keepAlive = keepAliveState,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            scope.launch {
                                delay(150)
                                viewModel.onEvent(
                                    HealthTrackerScreenEvent.DeleteTrackableFoodFromBeingTracked(
                                        foodItem
                                    )
                                )
                                val snackBarResult = snackbarHostState.showSnackbar(
                                    context.getString(R.string.do_you_want_go_back),
                                    context.getString(R.string.restore),
                                    SnackbarDuration.Long
                                )
                                when (snackBarResult) {
                                    SnackbarResult.Dismissed -> {
                                        keepAliveState.value = false
                                    }

                                    SnackbarResult.ActionPerformed -> {
                                        viewModel.onEvent(HealthTrackerScreenEvent.RestoreFoodItem)
                                        keepAliveState.value = true
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(spacing.spaceSmall))
                        AddButton(
                            modifier = Modifier
                                .height(50.dp)
                                .padding(horizontal = spacing.spaceSmall),
                            text = stringResource(
                                id = R.string.add_meal,
                                meal.name.asString(context = context)
                            ),
                            textColor = MaterialTheme.colors.primary
                        ) {
                            onNavigate(
                                meal.mealTimeType,
                                dateTime.value.year,
                                dateTime.value.monthValue,
                                dateTime.value.dayOfMonth
                            )
                        }
                    }
                }
            }
        }
    }
}