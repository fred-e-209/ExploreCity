package com.example.explorecity.api.classes.event

data class EventBody(
    val description: String,
    val displayname: String,
    val location: Location
)