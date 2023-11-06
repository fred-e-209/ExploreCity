package com.example.explorecity.api.models

class UserInformation {
    companion object {
        val instance: UserInformation by lazy {
            UserInformation()
        }
        var username = ""
        var password = ""
    }

    fun getUsername() = username

    fun getPassword() = password

    fun setUsername(input: String) {
        username = input
    }

    fun setPassword(input: String) {
        password = input
    }
}