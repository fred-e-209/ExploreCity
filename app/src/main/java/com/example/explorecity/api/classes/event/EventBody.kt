package com.example.explorecity.api.classes.event

data class EventBody(
    val description: String,
    val displayname: String,
    val location: String,
    val start: DateTimeBody,
    val end: DateTimeBody
)