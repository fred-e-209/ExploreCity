package com.example.explorecity.api.classes.event

import com.example.explorecity.api.classes.User.SingleUserDetail

data class SingleEventResponse(
    val address: String,
    val attendees: List<SingleUserDetail>,
    val attending: Boolean,
    val description: String,
    val displayname: String,
    val end: DateTimeBody,
    val host: SingleUserDetail,
    val coords: Location,
    val start: DateTimeBody,
    val venue: String
)

fun emptySingleEventResponse(): SingleEventResponse {
    return SingleEventResponse(
        address = "",
        attendees = emptyList(),
        attending = false,
        description = "",
        displayname = "",
        end = DateTimeBody(-1, -1, -1, -1, -1),
        host = SingleUserDetail("", -1, ""),
        coords = Location(0.0,0.0),
        start = DateTimeBody(-1, -1, -1, -1, -1),
        venue = ""
    )
}