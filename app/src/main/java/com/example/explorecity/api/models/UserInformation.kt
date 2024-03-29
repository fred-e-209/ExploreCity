package com.example.explorecity.api.models

import com.example.explorecity.api.classes.event.Location

class UserInformation {
    companion object {
        val instance: UserInformation by lazy {
            UserInformation()
        }
        var username = ""
        var password = ""
        var userID = -1
        var userDisplayName = ""

        var userLocation = Location(lat = 0.0, lon = 0.0)

        var userEventsForTheDay = mutableListOf<Int>()
    }

    fun getUsername() = username

    fun getPassword() = password

    fun setUsername(input: String) {
        username = input
    }

    fun setPassword(input: String) {
        password = input
    }

    fun getUserDisplayName(): String {
        return userDisplayName
    }

    fun setUserDisplayName(input: String) {
        userDisplayName = input
    }

    fun getUserID() = userID

    fun setUserID(input: Int) {
        userID = input
    }

    fun setUserLocation(lat: Double, lon: Double) {
        userLocation = Location(lat = lat, lon = lon)
    }

    fun getUserLocation(): Location {
        return userLocation
    }

    fun getUserEventIDs(): List<Int> {
        return userEventsForTheDay
    }

    fun addToUserEventIDs(eventID: Int) {
        userEventsForTheDay.add(eventID)
    }

    fun clearUserEventIDs() {
        userEventsForTheDay.clear()
    }
}