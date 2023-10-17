package com.voitov.tracker_presentation.health_tracker_screen.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TimePointResult(
    val date: LocalDate,
    val inTotalKkal: Int,
    val kkalInFats: Int,
    val kkalInCarbs: Int,
    val kkalInProteins: Int
): Parcelable