package com.example.explorecity.api.callers

import com.example.explorecity.api.classes.LoginValidResponse
import com.example.explorecity.api.classes.RegistrationBody
import com.example.explorecity.api.classes.RegistrationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterfaces {
    @GET("/auth")
    suspend fun validateLogin(): LoginValidResponse

    @POST("/auth")
    fun registerUser(@Body registrationBody: RegistrationBody): Call<RegistrationResponse>
}