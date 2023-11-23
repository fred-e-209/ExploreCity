package com.example.explorecity.api.classes.event

data class EventFilter(
    val query: String,
    val startDate: FilterStartDate,
    val endDate: FilterEndDate,
    val userLocation: Location,
    val radius: Int
)

data class FilterStartDate(
    val ey: Int,
    val em: Int,
    val ed: Int
)

data class FilterEndDate(
    val ly: Int,
    val lm: Int,
    val ld: Int
)
