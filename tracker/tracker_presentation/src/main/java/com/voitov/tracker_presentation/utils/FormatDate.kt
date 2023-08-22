package com.voitov.tracker_presentation.utils

import android.content.Context
import com.voitov.common.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDate(date: LocalDateTime, context: Context): String {
    val dateTime = date.toLocalDate()
    val currentTime = LocalDate.now()
    return when (dateTime) {
        currentTime.plusDays(1) -> context.getString(R.string.next_day)
        currentTime.minusDays(1) -> context.getString(R.string.previous_day)
        else -> DateTimeFormatter.ofPattern("dd LLLL yy").format(date)
    }
}