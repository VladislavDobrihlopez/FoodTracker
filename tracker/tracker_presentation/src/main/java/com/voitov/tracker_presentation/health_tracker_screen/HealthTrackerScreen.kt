package com.voitov.tracker_presentation.health_tracker_screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
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
import com.voitov.common.domain.UiEvents
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_presentation.components.AddButton
import com.voitov.tracker_presentation.components.MealItem
import com.voitov.tracker_presentation.components.TrackedFoodItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun HealthTrackerScreen(
    viewModel: HealthTrackerOverviewViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit
) {
    val spacing = LocalSpacing.current
    val screenState = viewModel.screenState
    val context = LocalContext.current
    Log.d("TEST_STATE", screenState.toString())

    val scope = remember { CoroutineScope(Dispatchers.Main.immediate) }
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent
            .onEach { event ->
                Log.d("TEST_CHANNEL", "delivered")
                when (event) {
                    is UiEvents.NavigateTo -> onNavigateTo(event.route)
                    else -> throw IllegalStateException()
                }
            }
            .launchIn(scope)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            NutrientOverviewHeader(screenState)
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
                val filteredItems = screenState.trackedFoods.filter { meal.mealType == it.mealType }
                LazyColumn(modifier = Modifier.height(100.dp * (filteredItems.size + 1))) {
                    items(filteredItems) { foodItem ->
                        TrackedFoodItem(
                            item = foodItem,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = spacing.spaceSmall)
                        ) {
                            viewModel.onEvent(
                                HealthTrackerScreenEvent.DeleteTrackableFoodFromBeingTracked(
                                    foodItem
                                )
                            )
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
                            viewModel.onEvent(
                                HealthTrackerScreenEvent.AddTrackableFoodToBeingTracked(
                                    meal
                                )
                            )
                        }
                    }
                }
            }

        }
    }
}