package com.example.explorecity.api.classes.auth

data class RegistrationBody(
    val displayname: String,
    val email: String,
    val password: String,
    val username: String
)