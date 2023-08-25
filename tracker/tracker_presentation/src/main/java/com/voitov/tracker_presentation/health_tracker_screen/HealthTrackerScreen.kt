package com.voitov.tracker_presentation.health_tracker_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.utils.UiEvents
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_presentation.components.AddButton
import com.voitov.tracker_presentation.components.MealItem
import com.voitov.tracker_presentation.components.TrackedFoodItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun HealthTrackerScreen(
    scaffoldState: ScaffoldState,
    viewModel: HealthTrackerOverviewViewModel = hiltViewModel(),
    onDoReonboarding: () -> Unit,
    onNavigate: (MealType, Int, Int, Int) -> Unit
) {
    val spacing = LocalSpacing.current
    val screenState = viewModel.screenState
    val context = LocalContext.current
    val appInfoDialogIsShownState = remember {
        mutableStateOf(false)
    }

    Log.d("TEST_STATE", screenState.toString())

    val eventScope = remember { CoroutineScope(Dispatchers.Main.immediate) }
    val scope = remember { CoroutineScope(Dispatchers.Main) }
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent
            .onEach { event ->
                Log.d("TEST_CHANNEL", "delivered")
                when (event) {
                    is UiEvents.ShowUpSnackBar -> {
                        scaffoldState.snackbarHostState.showSnackbar(event.text.asString(context))
                    }

                    else -> throw IllegalStateException()
                }
            }
            .launchIn(eventScope)
    }

    DeveloperAndAppInfo(
        isShownState = appInfoDialogIsShownState,
        onDismissClick = {
            appInfoDialogIsShownState.value = false
        },
        onOkayClick = {
            appInfoDialogIsShownState.value = false
        }
    )

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            NutrientOverviewHeader(
                state = screenState,
                onAppInfoClick = {
                    appInfoDialogIsShownState.value = true
                },
                onDoReonboardingClick = {
                    viewModel.onEvent(HealthTrackerScreenEvent.DoReonbording)
                    onDoReonboarding()
                }
            )
        }

        val dateTime = mutableStateOf(screenState.dateTime)

        item {
            DaySelector(
                date = dateTime,
                onPreviousDayClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToPreviousDay) },
                onNextDayClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToNextDay) },
                onPreviousWeekClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToWeekBehind) },
                onNextWeekClick = { viewModel.onEvent(HealthTrackerScreenEvent.NavigateToWeekAhead) })
        }

        items(screenState.mealsDuringCurrentDay) { meal ->
            MealItem(modifier = Modifier.clickable {
                viewModel.onEvent(
                    HealthTrackerScreenEvent.ToggleMeal(
                        mealType = meal.mealType
                    )
                )
            }, meal = meal)
            AnimatedVisibility(visible = meal.isExpanded) {
                val filteredItems =
                    screenState.trackedFoods.filter { meal.mealType == it.mealType }.toList()
                LazyColumn(modifier = Modifier.height(100.dp * (filteredItems.size + 1))) {
                    items(filteredItems, key = { it.id }) { foodItem ->
                        val keepAliveState = remember {
                            mutableStateOf<Boolean>(true)
                        }

                        TrackedFoodItem(
                            item = foodItem,
                            keepAlive = keepAliveState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = spacing.spaceSmall)
                        ) {
                            scope.launch {
                                delay(250)
                                viewModel.onEvent(
                                    HealthTrackerScreenEvent.DeleteTrackableFoodFromBeingTracked(
                                        foodItem
                                    )
                                )
                                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                                    "Do you want to return back?",
                                    "Restore",
                                    SnackbarDuration.Short
                                )
                                when (snackBarResult) {
                                    SnackbarResult.Dismissed -> {
                                        Toast.makeText(
                                            context,
                                            "Successfully deleted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        keepAliveState.value = false
                                    }

                                    SnackbarResult.ActionPerformed -> {
                                        viewModel.onEvent(HealthTrackerScreenEvent.RestoreFoodItem)
                                        keepAliveState.value = true
                                        Toast.makeText(
                                            context,
                                            "Successfully restored",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                    item {
                        AddButton(
                            modifier = Modifier.height(50.dp),
                            text = stringResource(
                                id = R.string.add_meal,
                                meal.name.asString(context = context)
                            ),
                            textColor = MaterialTheme.colors.primary
                        ) {
                            onNavigate(
                                meal.mealType,
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