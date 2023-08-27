package com.voitov.tracker_presentation.searching_for_food_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.R
import com.voitov.common.domain.use_cases.FilterOutDigitsUseCase
import com.voitov.common.utils.UiEvents
import com.voitov.common.utils.UiText
import com.voitov.tracker_domain.use_case.NutrientStuffUseCasesWrapper
import com.voitov.tracker_presentation.searching_for_food_screen.model.TrackableFoodUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFoodViewModel
@Inject constructor(
    private val trackerUseCases: NutrientStuffUseCasesWrapper,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {

    var screenState by mutableStateOf(SearchFoodScreenState())
        private set

    private val _uiChannel = Channel<UiEvents>()
    val uiEvent = _uiChannel.receiveAsFlow()

    fun onEvent(event: SearchFoodScreenEvent) {
        when (event) {
            is SearchFoodScreenEvent.OnAddTrackableFood -> {
                trackFood(event)
            }

            is SearchFoodScreenEvent.OnAmountForFoodChange -> {
                screenState = screenState.copy(food = screenState.food.map { uiModel ->
                    if (uiModel.food == event.food) {
                        uiModel.copy(amount = filterOutDigitsUseCase(event.amount))
                    } else {
                        uiModel
                    }
                })
            }

            is SearchFoodScreenEvent.OnSearch -> {
                performSearch(event)
            }

            is SearchFoodScreenEvent.OnSearchBarFocusChange -> {
                screenState =
                    screenState.copy(isHintVisible = !event.hasFocus && screenState.searchBarText.isBlank())
            }

            is SearchFoodScreenEvent.OnSearchTextChange -> {
                screenState =
                    screenState.copy(searchBarText = UiText.DynamicResource(event.foodName))
            }

            is SearchFoodScreenEvent.ToggleTrackableFoodItem -> {
                screenState = screenState.copy(food = screenState.food.map {
                    if (it.food == event.food) {
                        it.copy(isExpanded = !it.isExpanded)
                    } else {
                        it
                    }
                })
            }

            is SearchFoodScreenEvent.OnTapCountry -> {
                screenState =
                    screenState.copy(countrySearchSettings = screenState.countrySearchSettings.map {
                        if (it.country == event.country) {
                            it.copy(isSelected = !it.isSelected)
                        } else {
                            it.copy(isSelected = false)
                        }
                    })
            }
        }
    }

    private fun trackFood(event: SearchFoodScreenEvent.OnAddTrackableFood) {
        viewModelScope.launch {
            trackerUseCases.insertFoodUseCase(
                food = event.food.food,
                amount = event.food.amount.toIntOrNull() ?: return@launch,
                dateTime = event.date,
                mealType = event.mealType
            )
            _uiChannel.send(UiEvents.NavigateUp)
        }
    }

    private fun performSearch(event: SearchFoodScreenEvent.OnSearch) {
        viewModelScope.launch {
            screenState = screenState.copy(
                isSearchingGoingOn = true,
                food = emptyList()
            )

            trackerUseCases.searchFoodUseCase(
                event.searchText,
                country = screenState.countrySearchSettings.find { it.isSelected }?.country
                    ?: throw IllegalStateException()
            )
                .onSuccess { trackableFoodList ->
                    screenState =
                        screenState.copy(
                            searchBarText = UiText.DynamicResource(""),
                            isSearchingGoingOn = false,
                            food = trackableFoodList.map {
                                TrackableFoodUiModel(food = it)
                            })
                }
                .onFailure {
                    screenState = screenState.copy(isSearchingGoingOn = false)
                    _uiChannel.send(UiEvents.ShowUpSnackBar(UiText.StaticResource(R.string.error_unknown)))
                }
        }
    }
}