package com.example.explorecity.api.classes.event

data class EventDetailBody(
    val description: String,
    val displayname: String,
    val end: DateTimeBody,
    val id: Int,
    val start: DateTimeBody
)