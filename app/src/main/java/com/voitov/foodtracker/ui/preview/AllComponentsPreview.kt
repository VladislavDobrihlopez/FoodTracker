package com.voitov.foodtracker.ui.preview

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.voitov.foodtracker.ui.theme.FoodTrackerTheme
import com.voitov.onboarding_presentation.components.SelectionButton
import com.voitov.onboarding_presentation.components.UnitEditText
import com.voitov.tracker_presentation.components.AddButton
import com.voitov.tracker_presentation.components.EatenFoodOverviewHorizontalBar
import com.voitov.tracker_presentation.components.SearchBar
import com.voitov.tracker_presentation.components.UiNumberFollowedByUnit
import com.voitov.tracker_presentation.custom_food_screen.components.PhotoPicker
import com.voitov.tracker_presentation.health_tracker_screen.components.AppInfo
import com.voitov.tracker_presentation.health_tracker_screen.components.CustomBarChart
import com.voitov.tracker_presentation.health_tracker_screen.components.CustomBarChartState
import com.voitov.tracker_presentation.health_tracker_screen.model.TimePointResult
import java.time.LocalDate

@Preview(showBackground = true)
@Composable
internal fun PreviewUnitEditText() {
    FoodTrackerTheme {
        UnitEditText(value = "17", unit = "cm", onValueChange = {})
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewSelectionButton() {
    FoodTrackerTheme {
        SelectionButton(
            value = "first_option",
            {},
            isSelected = true,
            selectedTextColor = MaterialTheme.colors.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewSelectionButtonDisabled() {
    FoodTrackerTheme {
        SelectionButton(
            value = "first_option",
            {},
            isSelected = false,
            selectedTextColor = MaterialTheme.colors.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewUiNumberFollowedByUnit() {
    FoodTrackerTheme {
        UiNumberFollowedByUnit(amount = "1000", unit = "Kkcal")
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewEatenFoodOverview() {
    FoodTrackerTheme {
        EatenFoodOverviewHorizontalBar(
            calories = 100,
            caloriesGoal = 250,
            fat = 10,
            carbs = 20,
            proteins = 11
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewAddButton() {
    FoodTrackerTheme {
        AddButton(text = "Add item") {

        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewSearchButton() {
    FoodTrackerTheme {
        SearchBar(
            text = "",
            onValueChange = {},
            onFocusChange = {},
            onSearch = {},
            shouldShowHint = true,
            maxLines = 2
        ) {

        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
internal fun PreviewDeveloperAndAppInfo() {
    FoodTrackerTheme {
        AppInfo(
            isShownState = mutableStateOf(true),
            onOkayClick = {},
            onDismissClick = {})
    }
}

@Preview(showBackground = true)
@Composable
internal fun PreviewPhotoPicker() {
    FoodTrackerTheme {
        PhotoPicker(onContentClick = { })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
internal fun PreviewCustomBarChart() {
    FoodTrackerTheme {
        val items = buildList {
            add(TimePointResult(LocalDate.now(), 40, 30, 5, 5))
//            add(TimePointResult(LocalDate.now(), 10, 3, 3, 4))
//            add(TimePointResult(LocalDate.now(), 9, 7, 2, 1))
//            add(TimePointResult(LocalDate.now(), 18, 10, 4, 4))
//            add(TimePointResult(LocalDate.now(), 7, 3, 2, 2))
//            add(TimePointResult(LocalDate.now(), 11, 5, 4, 2))
//            add(TimePointResult(LocalDate.now(), 15, 2, 3, 10))
//            add(TimePointResult(LocalDate.now(), 5, 3, 1, 1))
//            add(TimePointResult(LocalDate.now(), 6, 3, 1, 2))
//            add(TimePointResult(LocalDate.now(), 40, 30, 5, 5))

        }
        CustomBarChart(
            nutrientGoalInKkal = 17,
            items = items,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            shouldDisplayInZoneArea = {
                true
            },
            shouldDisplayExceededArea = {
                true
            },
        )
    }
}