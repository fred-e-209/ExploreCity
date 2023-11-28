package com.example.explorecity.api.classes

import com.example.explorecity.api.classes.event.Location

data class RecommendationResponseBody(
    val address: String,
    val distance: Double, // In miles
    val location: Location,
    val name: String
)
