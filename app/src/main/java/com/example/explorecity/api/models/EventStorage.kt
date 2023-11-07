package com.example.explorecity.api.models

class EventStorage {
    companion object {
        val instance: EventStorage by lazy {
            EventStorage()
        }

        private var selectedEventID: Int = -1
    }

    fun setEventID(eventID: Int) {
        selectedEventID = eventID
    }

    fun getEventID(): Int {
        return selectedEventID
    }
}