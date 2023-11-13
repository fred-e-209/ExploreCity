package com.example.explorecity.api.classes.event

data class EventListResponse(
    val attending: List<EventDetailBody>,
    val hosting: List<EventDetailBody>
)