package com.voitov.tracker_presentation.searching_for_food_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.R
import com.voitov.common.domain.use_cases.FilterOutDigitsUseCase
import com.voitov.common.utils.UiSideEffect
import com.voitov.common.utils.UiText
import com.voitov.tracker_domain.model.Country
import com.voitov.tracker_domain.model.TrackableFoodSearchingType
import com.voitov.tracker_domain.use_case.wrapper.NutrientStuffUseCasesWrapper
import com.voitov.tracker_presentation.searching_for_food_screen.contract.SearchFoodScreenEvent
import com.voitov.tracker_presentation.searching_for_food_screen.contract.SearchFoodScreenState
import com.voitov.tracker_presentation.searching_for_food_screen.contract.TabSection
import com.voitov.tracker_presentation.searching_for_food_screen.contract.TabSectionScreenState
import com.voitov.tracker_presentation.searching_for_food_screen.mapper.toCustomTrackableFood
import com.voitov.tracker_presentation.searching_for_food_screen.model.TrackableFoodUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchFoodViewModel
@Inject constructor(
    private val trackerUseCases: NutrientStuffUseCasesWrapper,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {

    var screenState by mutableStateOf(SearchFoodScreenState())
        private set

    private val _uiChannel = Channel<UiSideEffect>(Channel.BUFFERED)
    val uiEvent = _uiChannel.receiveAsFlow()

    private val sectionScreenState: TabSectionScreenState
        get() = screenState.tabs[screenState.currentSelectedTab] ?: throw IllegalStateException()

    private fun updateSectionScreenData(
        tabSectionToScreenSection: Map<TabSection, TabSectionScreenState>,
    ) {
        screenState = screenState.copy(tabs = tabSectionToScreenSection)
    }

    private var searchJob: Job? = null

    fun onEvent(event: SearchFoodScreenEvent) {
        when (event) {
            is SearchFoodScreenEvent.OnAddTrackableFood -> {
                trackFood(event)
            }

            is SearchFoodScreenEvent.OnAmountForFoodChange -> {
                val sectionToScreenData = screenState.tabs.toMutableMap()
                val newSectionData =
                    sectionScreenState.copy(food = sectionScreenState.food.map { uiModel ->
                        if (uiModel.food == event.food) {
                            uiModel.copy(amount = filterOutDigitsUseCase(event.amount))
                        } else {
                            uiModel
                        }
                    })

                sectionToScreenData[screenState.currentSelectedTab] = newSectionData
                updateSectionScreenData(sectionToScreenData.toMap())
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
                    screenState.copy(searchBarText = event.foodName)
            }

            is SearchFoodScreenEvent.ToggleTrackableFoodItem -> {
                val sectionToScreenData = screenState.tabs.toMutableMap()
                val newSectionData =
                    sectionScreenState.copy(food = sectionScreenState.food.map { uiModel ->
                        if (uiModel.food == event.food) {
                            uiModel.copy(isExpanded = !uiModel.isExpanded)
                        } else {
                            uiModel
                        }
                    })
                sectionToScreenData[screenState.currentSelectedTab] = newSectionData
                updateSectionScreenData(sectionToScreenData.toMap())
            }

            is SearchFoodScreenEvent.OnTapCountry -> {
                changeCountry(event.country)
            }

            is SearchFoodScreenEvent.OnSelectTab -> {
                screenState = screenState.copy(currentSelectedTab = event.tabSection)
                if (event.tabSection.section == TrackableFoodSearchingType.LOCAL) {
                    performSearch(SearchFoodScreenEvent.OnSearch(searchText = ""))
                }
            }

            is SearchFoodScreenEvent.OnDeleteLocalFood -> {
                if (screenState.currentSelectedTab.section != TrackableFoodSearchingType.LOCAL) {
                    throw IllegalStateException()
                }

                viewModelScope.launch {
                    trackerUseCases.deleteTrackableCustomFoodUseCase(
                        event.item.toCustomTrackableFood(LocalDateTime.now())
                    )
                }

                val sectionToScreenData = screenState.tabs.toMutableMap()
                val newSectionScreenState = sectionScreenState.copy(
                    food = sectionScreenState.food.toMutableList().also { it.remove(event.item) })
                sectionToScreenData[screenState.currentSelectedTab] = newSectionScreenState
                updateSectionScreenData(sectionToScreenData)
            }
        }
    }

    private fun changeCountry(country: Country) {
        var countrySearchSettings = screenState.countrySearchSettings.map {
            if (it.country == country) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it.copy(isSelected = false)
            }
        }

        if (!countrySearchSettings.any { it.isSelected }) {
            countrySearchSettings = countrySearchSettings.map {
                if (it.country == Country.WORLD) {
                    it.copy(isSelected = true)
                } else {
                    it
                }
            }
        }

        screenState = screenState.copy(countrySearchSettings = countrySearchSettings)
    }

    private fun trackFood(event: SearchFoodScreenEvent.OnAddTrackableFood) {
        viewModelScope.launch {
            trackerUseCases.insertTrackableFoodUseCase(
                food = event.food.food,
                amount = event.food.amount.toIntOrNull() ?: return@launch,
                dateTime = event.date,
                mealType = event.mealType
            )
            _uiChannel.send(UiSideEffect.NavigateUp)
        }
    }

    private fun performSearch(event: SearchFoodScreenEvent.OnSearch) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            screenState = screenState.copy(
                isSearchingGoingOn = true
            )

            when (screenState.currentSelectedTab.section) {
                TrackableFoodSearchingType.LOCAL -> {
                    trackerUseCases.searchCustomTrackableFoodUseCase(event.searchText)
                        .catch {
                            screenState = screenState.copy(isSearchingGoingOn = false)
                            _uiChannel.send(UiSideEffect.ShowUpSnackBar(UiText.StaticResource(R.string.error_unknown)))
                        }
                        .collect { trackableFoodList ->
                            val sectionToScreenData = screenState.tabs.toMutableMap()
                            val newSectionScreenState =
                                sectionScreenState.copy(
                                    food = trackableFoodList.map {
                                        TrackableFoodUiModel(food = it.trackableFood)
                                    })
                            sectionToScreenData[screenState.currentSelectedTab] =
                                newSectionScreenState
                            updateSectionScreenData(sectionToScreenData)
                            screenState = screenState.copy(
                                searchBarText = "",
                                isSearchingGoingOn = false,
                            )
                        }
                }

                TrackableFoodSearchingType.INTERNET -> {
                    trackerUseCases.searchTrackableFoodUseCase(
                        event.searchText,
                        country = screenState.countrySearchSettings.find { it.isSelected }?.country
                            ?: throw IllegalStateException()
                    )
                        .onSuccess { trackableFoodList ->
                            val sectionToScreenData = screenState.tabs.toMutableMap()
                            val newSectionScreenState =
                                sectionScreenState.copy(
                                    food = trackableFoodList.map {
                                        TrackableFoodUiModel(food = it)
                                    })
                            sectionToScreenData[screenState.currentSelectedTab] =
                                newSectionScreenState
                            updateSectionScreenData(sectionToScreenData)
                            screenState = screenState.copy(
                                searchBarText = "",
                                isSearchingGoingOn = false,
                            )
                        }
                        .onFailure {
                            screenState = screenState.copy(isSearchingGoingOn = false)
                            _uiChannel.send(UiSideEffect.ShowUpSnackBar(UiText.StaticResource(R.string.error_unknown)))
                        }
                }
            }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }
}