package com.example.explorecity.api.callers

import com.example.explorecity.api.classes.auth.LoginValidResponse
import com.example.explorecity.api.classes.auth.RegistrationBody
import com.example.explorecity.api.classes.auth.RegistrationResponse
import com.example.explorecity.api.classes.event.EventBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterfaces {
    @GET("/auth")
    suspend fun validateLogin(): LoginValidResponse

    @POST("/auth")
    fun registerUser(@Body registrationBody: RegistrationBody): Call<RegistrationResponse>

    @POST("/event")
    suspend fun createNewEvent(@Body eventBody: EventBody): RegistrationResponse
}