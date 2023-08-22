package com.voitov.tracker_presentation.health_tracker_screen.model

import androidx.annotation.DrawableRes
import com.voitov.common.R
import com.voitov.common.domain.UiText
import com.voitov.tracker_domain.model.MealType

data class Meal(
    val name: UiText,
    @DrawableRes val pictureResId: Int,
    val mealType: MealType,
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
        mealType = MealType.BREAKFAST
    ),
    Meal(
        name = UiText.StaticResource(R.string.brunch),
        pictureResId = R.drawable.brunch,
        mealType = MealType.BRUNCH
    ),
    Meal(
        name = UiText.StaticResource(R.string.lunch),
        pictureResId = R.drawable.lunch,
        mealType = MealType.LUNCH
    ),
    Meal(
        name = UiText.StaticResource(R.string.supper),
        pictureResId = R.drawable.supper,
        mealType = MealType.SUPPER
    ),
    Meal(
        name = UiText.StaticResource(R.string.dinner),
        pictureResId = R.drawable.dinner,
        mealType = MealType.DINNER
    ),
    Meal(
        name = UiText.StaticResource(R.string.snacks),
        pictureResId = R.drawable.snack,
        mealType = MealType.SNACK
    ),
)