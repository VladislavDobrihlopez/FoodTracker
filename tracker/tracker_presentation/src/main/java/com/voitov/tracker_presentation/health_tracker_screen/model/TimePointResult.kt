package com.voitov.tracker_presentation.health_tracker_screen.model

import java.time.LocalDate

data class TimePointResult(
    val date: LocalDate,
    val inTotalKkal: Int,
    val kkalInFats: Int,
    val kkalInCarbs: Int,
    val kkalInProteins: Int
)