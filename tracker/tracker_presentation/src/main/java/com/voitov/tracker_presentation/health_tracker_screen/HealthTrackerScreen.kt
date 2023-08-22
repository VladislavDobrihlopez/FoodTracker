package com.voitov.tracker_presentation.health_tracker_screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.tracker_presentation.components.MealItem

@Composable
fun HealthTrackerScreen(viewModel: HealthTrackerOverviewViewModel = hiltViewModel()) {
    val screenState = viewModel.screenState
    Log.d("TEST_STATE", screenState.toString())
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

        items(screenState.mealsDuringCurrentDay) { item ->
            MealItem(meal = item)
        }
    }
}