package com.voitov.tracker_presentation.searching_for_food_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.domain.UiEvents
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_presentation.components.SearchBar
import com.voitov.tracker_presentation.searching_for_food_screen.components.TrackableFoodUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    mealType: MealType,
    day: Int,
    month: Int,
    year: Int,
    viewModel: SearchFoodViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val scope = remember { CoroutineScope(Dispatchers.Main.immediate) }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent
            .onEach { event ->
                Log.d("TEST_CHANNEL", "delivered")
                when (event) {
                    is UiEvents.ShowUpSnackBar -> {
                        scaffoldState.snackbarHostState.showSnackbar(event.text.asString(context))
                        keyboardController?.hide()
                    }

                    is UiEvents.NavigateUp -> {
                        onNavigateUp()
                    }

                    else -> throw IllegalStateException()
                }
            }
            .launchIn(scope)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            viewModel.screenState.isSearchingGoingOn -> CircularProgressIndicator()
            viewModel.screenState.food.isEmpty() ->
                Text(
                    text = stringResource(id = R.string.no_results),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1
                )
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceSmall),
                text = viewModel.screenState.searchBarText.asString(context),
                onValueChange = {
                    viewModel.onEvent(SearchFoodScreenEvent.OnSearchTextChange(it))
                },
                onFocusChange = {
                    viewModel.onEvent(SearchFoodScreenEvent.OnSearchBarFocusChange(it.hasFocus))
                },
                onSearch = {
                    viewModel.onEvent(SearchFoodScreenEvent.OnSearch(it))
                },
                shouldShowHint = viewModel.screenState.isHintVisible,
            )
        }
        item {
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }
        items(viewModel.screenState.food) { foodUi ->
            TrackableFoodUi(foodUiModel = foodUi, onClick = {
                viewModel.onEvent(SearchFoodScreenEvent.ToggleTrackableFoodItem(foodUi.food))
            }, onAmountChange = {
                viewModel.onEvent(SearchFoodScreenEvent.OnAmountForFoodChange(it, foodUi.food))
            }, onCache = {
                val localTime = LocalTime.now()
                viewModel.onEvent(
                    SearchFoodScreenEvent.OnAddTrackableFood(
                        foodUi,
                        mealType,
                        LocalDateTime.of(year, month, day, localTime.hour, localTime.minute)
                    )
                )
            })
        }
    }
}