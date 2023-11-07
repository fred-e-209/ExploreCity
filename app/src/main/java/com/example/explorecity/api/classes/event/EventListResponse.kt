package com.example.explorecity.api.classes.event

data class EventListResponse(
    val attending: List<Any>,
    val hosting: List<Hosting>
)