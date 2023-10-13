package com.voitov.tracker_presentation.health_tracker_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
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
import com.voitov.tracker_presentation.health_tracker_screen.components.CustomBarChart
import com.voitov.tracker_presentation.health_tracker_screen.components.DaySelector
import com.voitov.tracker_presentation.health_tracker_screen.components.NutrientOverviewHeader
import com.voitov.tracker_presentation.health_tracker_screen.components.ScreenMode
import com.voitov.tracker_presentation.health_tracker_screen.components.ScreenTopBar
import com.voitov.tracker_presentation.health_tracker_screen.model.TimePointResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalAnimationApi::class)
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

    val areTopBarActionsExpanded = viewModel.screenState.areTopBarActionsExpanded

    val dateTime = remember(screenState.dateTime) {
        mutableStateOf(screenState.dateTime)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            ScreenTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary),
                isExpanded = areTopBarActionsExpanded,
                doReonboarding = {
                    viewModel.onEvent(HealthTrackerScreenEvent.DoReonbording)
                    onDoReonboarding()
                },
                viewExplanations = {
                    appInfoDialogIsShownState.value = true
                },
                shouldExpand = {
                    viewModel.onEvent(HealthTrackerScreenEvent.ToggleTopBar)
                },
                currentMode = viewModel.screenState.currentMode,
                switchMode = {
                    viewModel.onEvent(HealthTrackerScreenEvent.MoveOnToMode(it))
                },
            )
            AnimatedContent(targetState = screenState.currentMode, label = "") {
                when (it) {
                    ScreenMode.CHART -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(
                                        bottomStart = 45.dp,
                                        bottomEnd = 45.dp
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colors.primary),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Row {
                                    Checkbox(
                                        checked = screenState.chartState.showExceeding,
                                        onCheckedChange = {
                                            viewModel.onEvent(
                                                HealthTrackerScreenEvent.ChangeChartConfig(
                                                    showExceeding = it,
                                                    showAvg = screenState.chartState.showAvgValue
                                                )
                                            )
                                        }
                                    )
                                    Text(text = "Превышение")
                                }
                                Row {
                                    Checkbox(
                                        checked = screenState.chartState.showAvgValue,
                                        onCheckedChange = {
                                            viewModel.onEvent(
                                                HealthTrackerScreenEvent.ChangeChartConfig(
                                                    showExceeding = screenState.chartState.showExceeding,
                                                    showAvg = it
                                                )
                                            )
                                        }
                                    )
                                    Text(text = "Мой средний результат")
                                }
                            }
                            CustomBarChart(
                                modifier = Modifier
                                    .height(400.dp)
                                    .fillMaxWidth()
                                    .clipToBounds(),
                                nutrientGoalInKkal = screenState.headerState.caloriesPerDayGoal, //screenState.headerState.caloriesPerDayGoal,
                                shouldDisplayInZoneArea = {
                                    screenState.chartState.showAvgValue
                                },
                                shouldDisplayExceededArea = {
                                    screenState.chartState.showExceeding
                                },
                                items = screenState.chartState.dataPoints
//                                buildList {
//                                    add(TimePointResult(LocalDate.now(), 10, 3, 3, 4))
//                                    add(TimePointResult(LocalDate.now(), 9, 6, 2, 1))
//                                    add(TimePointResult(LocalDate.now(), 18, 9, 3, 6))
//                                    add(TimePointResult(LocalDate.now(), 7, 3, 2, 2))
//                                    add(TimePointResult(LocalDate.now(), 11, 5, 4, 2))
//                                    add(TimePointResult(LocalDate.now(), 15, 2, 3, 10))
//                                    add(TimePointResult(LocalDate.now(), 5, 3, 1, 1))
//                                    add(TimePointResult(LocalDate.now(), 6, 3, 1, 2))
//                                    add(TimePointResult(LocalDate.now(), 40, 30, 5, 5))
//                                }
                            )
                        }
                    }

                    ScreenMode.HOME -> {
                        NutrientOverviewHeader(
                            state = screenState.headerState, modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(bottomStart = 45.dp, bottomEnd = 45.dp))
                                .background(MaterialTheme.colors.primary)
                        )
                    }
                }
            }

            Spacer(Modifier.height(spacing.spaceMedium))
            DaySelector(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceSmall),
                date = dateTime,
                onPreviousDayClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToPreviousDay) },
                onNextDayClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToNextDay) },
                onPreviousWeekClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToWeekBehind) },
                onNextWeekClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToWeekAhead) }
            )
        }

        items(screenState.mealsDuringCurrentDay, key = { it.mealTimeType }) { meal ->
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
                    screenState.trackedFoods.filter { meal.mealTimeType == it.mealTimeType }
                        .toList()
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