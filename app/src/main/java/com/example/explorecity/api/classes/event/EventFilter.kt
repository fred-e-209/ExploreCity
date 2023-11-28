package com.example.explorecity.api.classes.event

data class EventFilter(
    val query: String?,
    val startDate: DateTimeBody,
    val endDate: DateTimeBody,
    val userLocation: Location,
    val radius: Int
)
