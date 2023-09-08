package com.voitov.tracker_domain.model

import java.time.LocalDateTime

data class CustomTrackableFood(
    val id : Int = 0,
    val trackableFood: TrackableFood,
    val date: LocalDateTime
)