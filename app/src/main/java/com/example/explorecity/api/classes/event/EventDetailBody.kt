package com.example.explorecity.api.classes.event

import com.example.explorecity.api.classes.auth.User

data class EventDetailBody(
    val id: Int,
    val displayname: String,
    val description: String,
    val start: DateTimeBody,
    val end: DateTimeBody,
    val coords: Location,
    val address: String,
    val venue: String,
    val attendees: List<Int>,
    val attending: Boolean,
    val host: User,
)