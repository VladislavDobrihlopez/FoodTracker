package com.voitov.tracker_presentation.searching_for_food_screen.contract

import com.voitov.tracker_domain.model.Country
import com.voitov.tracker_domain.model.MealPhysicsType
import com.voitov.tracker_domain.model.MealTimeType
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_presentation.searching_for_food_screen.model.TrackableFoodUiModel
import java.time.LocalDateTime

sealed class SearchFoodScreenEvent {
    data class OnSearchBarFocusChange(val hasFocus: Boolean) : SearchFoodScreenEvent()
    data class OnSearchTextChange(val foodName: String) : SearchFoodScreenEvent()
    data class OnSearch(val searchText: String) : SearchFoodScreenEvent()
    data class ToggleTrackableFoodItem(val food: TrackableFood) : SearchFoodScreenEvent()
    data class OnAmountForFoodChange(val amount: String, val food: TrackableFood) :
        SearchFoodScreenEvent()

    data class OnAddTrackableFood(
        val food: TrackableFoodUiModel,
        val mealTimeType: MealTimeType,
        val date: LocalDateTime
    ) : SearchFoodScreenEvent()

    data class OnTapCountry(val country: Country) : SearchFoodScreenEvent()
    data class OnSelectTab(val tabSection: TabSection) : SearchFoodScreenEvent()
    data class OnDeleteLocalFood(val item: TrackableFoodUiModel) : SearchFoodScreenEvent()
    data class OnChangeOrderContent(val item: TrackableFoodUiModel, val mealType: MealPhysicsType, val shouldBeAdded: Boolean) :
        SearchFoodScreenEvent()
    data class OnClearOrder(val item: TrackableFoodUiModel): SearchFoodScreenEvent()
}
