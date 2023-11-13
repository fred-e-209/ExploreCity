package com.example.explorecity.api.classes.event

import com.example.explorecity.api.classes.User.SingleUserDetail

data class SingleEventResponse(
    val attendees: List<SingleUserDetail>,
    val attending: Boolean,
    val description: String,
    val displayname: String,
    val end: DateTimeBody,
    val host: SingleUserDetail,
    val location: Location,
    val start: DateTimeBody
)

fun emptySingleEventResponse(): SingleEventResponse {
    return SingleEventResponse(
        attendees = emptyList(),
        attending = false,
        description = "",
        displayname = "",
        end = DateTimeBody(-1, -1, -1, -1, -1),
        host = SingleUserDetail("", -1, ""),
        location = Location(0.0, 0.0),
        start = DateTimeBody(-1, -1, -1, -1, -1)
    )
}