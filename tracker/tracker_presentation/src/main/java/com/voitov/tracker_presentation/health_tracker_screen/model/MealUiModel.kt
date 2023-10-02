package com.voitov.tracker_presentation.health_tracker_screen.model

import androidx.annotation.DrawableRes
import com.voitov.common.R
import com.voitov.common.utils.UiText
import com.voitov.tracker_domain.model.MealTimeType

data class Meal(
    val name: UiText,
    @DrawableRes val pictureResId: Int,
    val mealTimeType: MealTimeType,
    val calories: Int = 0,
    val carbohydrates: Int = 0,
    val fat: Int = 0,
    val protein: Int = 0,
    val isExpanded: Boolean = false
)

val allDayMealsByDefault = listOf<Meal>(
    Meal(
        name = UiText.StaticResource(R.string.breakfast),
        pictureResId = R.drawable.breakfast,
        mealTimeType = MealTimeType.BREAKFAST
    ),
    Meal(
        name = UiText.StaticResource(R.string.brunch),
        pictureResId = R.drawable.brunch,
        mealTimeType = MealTimeType.BRUNCH
    ),
    Meal(
        name = UiText.StaticResource(R.string.lunch),
        pictureResId = R.drawable.lunch,
        mealTimeType = MealTimeType.LUNCH
    ),
    Meal(
        name = UiText.StaticResource(R.string.supper),
        pictureResId = R.drawable.supper,
        mealTimeType = MealTimeType.SUPPER
    ),
    Meal(
        name = UiText.StaticResource(R.string.dinner),
        pictureResId = R.drawable.dinner,
        mealTimeType = MealTimeType.DINNER
    ),
    Meal(
        name = UiText.StaticResource(R.string.snacks),
        pictureResId = R.drawable.snack,
        mealTimeType = MealTimeType.SNACK
    ),
)