package com.voitov.tracker_domain.model

import java.time.LocalDateTime

data class CustomTrackableFood(
    val trackableFood: TrackableFood,
    val date: LocalDateTime
)