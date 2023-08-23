package com.voitov.tracker_presentation.searching_for_food_screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.domain.UiEvents
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_presentation.components.SearchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun SearchScreen(
    mealType: MealType,
    day: Int,
    dayOfMonth: Int,
    year: Int,
    viewModel: SearchFoodViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val scope = remember { CoroutineScope(Dispatchers.Main.immediate) }
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent
            .onEach { event ->
                Log.d("TEST_CHANNEL", "delivered")
                when (event) {
                    UiEvents.DispatchNavigationRequest -> onNavigateUp()
                    else -> throw IllegalStateException()
                }
            }
            .launchIn(scope)
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
    }
}