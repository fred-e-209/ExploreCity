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

        var userLocation = Location(lat = 0.0, lon = 0.0)
    }

    fun getUsername() = username

    fun getPassword() = password

    fun setUsername(input: String) {
        username = input
    }

    fun setPassword(input: String) {
        password = input
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
}