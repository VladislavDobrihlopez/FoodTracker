package com.voitov.tracker_presentation.searching_for_food_screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voitov.common.R
import com.voitov.common.utils.UiEvents
import com.voitov.common_ui.LocalSpacing
import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_presentation.components.SearchBar
import com.voitov.tracker_presentation.searching_for_food_screen.components.SearchConfigChip
import com.voitov.tracker_presentation.searching_for_food_screen.components.TrackableFoodUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalLayoutApi::class
)
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
    val scopeAsync = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

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

    val screenState = viewModel.screenState

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetState = sheetState,
        sheetContent = {
            if (sheetState.isVisible) {
                BackHandler {
                    scopeAsync.launch {
                        sheetState.hide()
                    }
                }
            }

            Column(modifier = Modifier.padding(spacing.spaceMedium)) {
                Text(
                    text = context.getString(R.string.search_settings),
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                Text(
                    text = context.getString(R.string.country),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    screenState.countrySearchSettings.forEach { countryConfig ->
                        SearchConfigChip(
                            modifier = Modifier,
                            onClick = {
                                viewModel.onEvent(
                                    SearchFoodScreenEvent.OnTapCountry(
                                        countryConfig.country
                                    )
                                )
                            },
                            textStyle = MaterialTheme.typography.button,
                            isSelected = countryConfig.isSelected,
                            unSelectedText = countryConfig.country.shortenCode,
                            selectedText = countryConfig.name.asString(context),
                            imageResId = countryConfig.pictureResId
                        )
                    }
                }
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
            }
        }) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                screenState.isSearchingGoingOn -> CircularProgressIndicator()
                screenState.food.isEmpty() ->
                    Text(
                        text = stringResource(id = R.string.no_results),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body1
                    )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.spaceSmall)
        ) {
            Text(
                text = stringResource(
                    id = R.string.add_meal, stringResource(
                        id = when (mealType) {
                            MealType.BREAKFAST -> R.string.breakfast
                            MealType.BRUNCH -> R.string.brunch
                            MealType.LUNCH -> R.string.lunch
                            MealType.SUPPER -> R.string.supper
                            MealType.DINNER -> R.string.dinner
                            MealType.SNACK -> R.string.snacks
                        }
                    ).lowercase()
                ),
                style = MaterialTheme.typography.h1,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    text = screenState.searchBarText.asString(context),
                    onValueChange = {
                        viewModel.onEvent(SearchFoodScreenEvent.OnSearchTextChange(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(SearchFoodScreenEvent.OnSearchBarFocusChange(it.hasFocus))
                    },
                    onSearch = {
                        keyboardController?.hide()
                        viewModel.onEvent(SearchFoodScreenEvent.OnSearch(it))
                    },
                    shouldShowHint = viewModel.screenState.isHintVisible,
                ) {
                    Spacer(modifier = Modifier.width(spacing.spaceExtraSmall))

                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            scopeAsync.launch { sheetState.show() }
                        }
                    ) {
                        Icon(
                            Icons.Outlined.MoreVert,
                            contentDescription = "menu"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(screenState.food) { foodUi ->
                    TrackableFoodUi(foodUiModel = foodUi, onClick = {
                        viewModel.onEvent(SearchFoodScreenEvent.ToggleTrackableFoodItem(foodUi.food))
                    }, onAmountChange = {
                        viewModel.onEvent(
                            SearchFoodScreenEvent.OnAmountForFoodChange(
                                it,
                                foodUi.food
                            )
                        )
                    }, onCache = {
                        keyboardController?.hide()
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
    }
}