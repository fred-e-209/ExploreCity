package com.example.explorecity.api.classes.event

data class SingleEventResponse(
    val attendees: List<Any>,
    val attending: Boolean,
    val description: String,
    val displayname: String,
    val location: Location
)

fun emptySingleEventResponse(): SingleEventResponse {
    return SingleEventResponse(
        attendees = emptyList(),
        attending = false,
        description = "",
        displayname = "",
        location = Location(0.0, 0.0)
    )
}