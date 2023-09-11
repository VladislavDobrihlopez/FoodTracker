package com.voitov.tracker_presentation.health_tracker_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voitov.common.R
import com.voitov.common.utils.UiSideEffect
import com.voitov.common.utils.UiText
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.tracker_domain.model.TrackedFood
import com.voitov.tracker_domain.use_case.wrapper.NutrientStuffUseCasesWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class HealthTrackerOverviewViewModel @Inject constructor(
    private val useCase: NutrientStuffUseCasesWrapper,
    private val keyValueStorage: UserInfoKeyValueStorage
) : ViewModel() {
    var screenState by mutableStateOf(HealthTrackerScreenState())
        private set

    private val _uiChannel = Channel<UiSideEffect>()
    val uiEvent = _uiChannel.receiveAsFlow()

    private var ongoingRefreshingJob: Job? = null

    init {
        keyValueStorage.saveWhetherOnboardingIsRequired(false)
        refreshScreenDataForCurrentDay()
    }

    private val lastDeletedItems = Stack<TrackedFood>()

    fun onEvent(event: HealthTrackerScreenEvent) {
        when (event) {
            HealthTrackerScreenEvent.NavigateToWeekAhead -> {
                updateWithDateDifferenceOfDays(NEXT_WEEK_DAY)
                refreshScreenDataForCurrentDay()
            }

            HealthTrackerScreenEvent.NavigateToWeekBehind -> {
                updateWithDateDifferenceOfDays(PREVIOUS_WEEK_DAY)
                refreshScreenDataForCurrentDay()
            }

            HealthTrackerScreenEvent.NavigateToNextDay -> {
                updateWithDateDifferenceOfDays(NEXT_DAY)
                refreshScreenDataForCurrentDay()
            }

            HealthTrackerScreenEvent.NavigateToPreviousDay -> {
                updateWithDateDifferenceOfDays(PREVIOUS_DAY)
                refreshScreenDataForCurrentDay()
            }

            is HealthTrackerScreenEvent.ToggleMeal -> {
                val oldMeal =
                    screenState.mealsDuringCurrentDay.find { it.mealType == event.mealType }
                //todo add error throwing
                oldMeal?.let {
                    val index = screenState.mealsDuringCurrentDay.indexOf(oldMeal)
                    screenState = screenState.copy(
                        mealsDuringCurrentDay = screenState.mealsDuringCurrentDay.toMutableList()
                            .apply {
                                removeAt(index)
                                add(index, oldMeal.copy(isExpanded = !oldMeal.isExpanded))
                            })
                }
            }

            is HealthTrackerScreenEvent.DoReonbording -> {
                keyValueStorage.saveWhetherOnboardingIsRequired(true)
            }

            is HealthTrackerScreenEvent.DeleteTrackableFoodFromBeingTracked -> {
                viewModelScope.launch {
                    useCase.deleteTrackedFoodUseCase(event.foodItem)
                    lastDeletedItems.push(event.foodItem)
                    refreshScreenDataForCurrentDay()
                }
            }

            HealthTrackerScreenEvent.RestoreFoodItem -> {
                viewModelScope.launch {
                    useCase.restoreFoodUseCase(lastDeletedItems.pop())
                    refreshScreenDataForCurrentDay()
                }
            }

            HealthTrackerScreenEvent.ToggleTopBar -> {
                screenState = screenState.copy(areTopBarActionsExpanded = !screenState.areTopBarActionsExpanded)
            }
        }
    }

    private fun refreshScreenDataForCurrentDay() {
        ongoingRefreshingJob?.cancel()
        ongoingRefreshingJob = useCase.retrieveAllTrackedFoodOnDateUseCase(screenState.dateTime)
            .onEach { trackedFoods ->
                val nutrientCalculationsResult = useCase.doNutrientMathUseCase(trackedFoods)
                screenState = screenState.copy(
                    caloriesPerDayGoal = nutrientCalculationsResult.caloriesPerDayGoal,
                    caloriesPerDayInFact = nutrientCalculationsResult.caloriesPerDayInFact,
                    carbsPerDayGoal = nutrientCalculationsResult.carbsPerDayGoal,
                    carbsPerDayInFact = nutrientCalculationsResult.carbsPerDayInFact,
                    fatPerDayGoal = nutrientCalculationsResult.fatPerDayGoal,
                    fatPerDayInFact = nutrientCalculationsResult.fatPerDayInFact,
                    proteinsPerDayGoal = nutrientCalculationsResult.proteinsPerDayGoal,
                    proteinsPerDayInFact = nutrientCalculationsResult.proteinsPerDayInFact,
                    dateTime = screenState.dateTime,
                    trackedFoods = trackedFoods,
                    mealsDuringCurrentDay = screenState.mealsDuringCurrentDay.map { oldMeal ->
                        val trackedFoodForMealType =
                            nutrientCalculationsResult.mealTimeToNutrients[oldMeal.mealType]
                                ?: return@map oldMeal.copy(
                                    calories = 0,
                                    carbohydrates = 0,
                                    fat = 0,
                                    protein = 0
                                )
                        oldMeal.copy(
                            calories = trackedFoodForMealType.calories,
                            carbohydrates = trackedFoodForMealType.carbs,
                            fat = trackedFoodForMealType.fat,
                            protein = trackedFoodForMealType.proteins
                        )
                    }
                )
            }
            .catch {
                _uiChannel.send(UiSideEffect.ShowUpSnackBar(UiText.StaticResource(R.string.error_unknown)))
            }
            .launchIn(viewModelScope)
    }

    private fun updateWithDateDifferenceOfDays(days: Long) {
        screenState = screenState.copy(
            dateTime = if (days < 0) {
                screenState.dateTime.minusDays(abs(days))
            } else {
                screenState.dateTime.plusDays(abs(days))
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        ongoingRefreshingJob?.cancel()
    }

    companion object {
        private const val NEXT_DAY = 1L
        private const val PREVIOUS_DAY = -1L
        private const val NEXT_WEEK_DAY = 7L
        private const val PREVIOUS_WEEK_DAY = -7L
    }
}