package com.voitov.tracker_presentation.searching_for_food_screen.model

import androidx.annotation.DrawableRes
import com.voitov.common.R
import com.voitov.common.utils.UiText
import com.voitov.tracker_domain.model.Country

data class SearchConfigUiModel(
    @DrawableRes val pictureResId: Int,
    val name: UiText,
    val country: Country,
    val isSelected: Boolean = false
)

val allCountriesByDefault = listOf<SearchConfigUiModel>(
    SearchConfigUiModel(
        pictureResId = R.drawable.belarus,
        country = Country.BELARUS,
        name = UiText.StaticResource(R.string.belarus)
    ),
    SearchConfigUiModel(
        pictureResId = R.drawable.ukraine,
        country = Country.UKRAINE,
        name = UiText.StaticResource(R.string.ukraine)
    ),
    SearchConfigUiModel(
        pictureResId = R.drawable.poland,
        country = Country.POLAND,
        name = UiText.StaticResource(R.string.poland)
    ),
    SearchConfigUiModel(
        pictureResId = R.drawable.germany,
        country = Country.GERMANY,
        name = UiText.StaticResource(R.string.germany)
    ),
    SearchConfigUiModel(
        pictureResId = R.drawable.russia,
        country = Country.RUSSIA,
        name = UiText.StaticResource(R.string.russia)
    ),
    SearchConfigUiModel(
        pictureResId = R.drawable.united_kingdom,
        country = Country.UK,
        name = UiText.StaticResource(R.string.uk)
    ),
    SearchConfigUiModel(
        pictureResId = R.drawable.usa,
        country = Country.USA,
        name = UiText.StaticResource(R.string.usa)
    ),
    SearchConfigUiModel(
        pictureResId = R.drawable.earth,
        country = Country.WORLD,
        name = UiText.StaticResource(R.string.world),
        isSelected = true
    )
)

