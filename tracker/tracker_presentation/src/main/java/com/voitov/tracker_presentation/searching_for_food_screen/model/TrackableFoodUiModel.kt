package com.voitov.tracker_presentation.searching_for_food_screen.model

import android.os.Parcelable
import com.voitov.tracker_domain.model.TrackableFood
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackableFoodUiModel(
    val food: TrackableFood,
    val amount: String = "",
    val isExpanded: Boolean = false,
): Parcelable