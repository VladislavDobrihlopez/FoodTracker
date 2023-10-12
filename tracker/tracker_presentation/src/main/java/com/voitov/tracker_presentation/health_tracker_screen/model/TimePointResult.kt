package com.voitov.tracker_presentation.health_tracker_screen.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class TimePointResult(val date: LocalDate, val total: Int, val fat: Int, val carbs: Int, val proteins: Int)