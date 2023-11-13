package com.example.explorecity.api.callers

import com.example.explorecity.api.classes.auth.LoginValidResponse
import com.example.explorecity.api.classes.auth.RegistrationBody
import com.example.explorecity.api.classes.auth.RegistrationResponse
import com.example.explorecity.api.classes.event.EventBody
import com.example.explorecity.api.classes.event.EventListResponse
import com.example.explorecity.api.classes.event.SingleEventResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiInterfaces {
    @GET("/auth")
    suspend fun validateLogin(): LoginValidResponse

    @POST("/auth")
    fun registerUser(@Body registrationBody: RegistrationBody): Call<RegistrationResponse>

    @GET("/event")
    suspend fun getUserEvents(): EventListResponse

    @POST("/event")
    suspend fun createNewEvent(@Body eventBody: EventBody): RegistrationResponse

    @GET("/event/{id}")
    suspend fun getSingleEvent(@Path("id") id: Int): SingleEventResponse

    @PUT("/event/{eventID}/user/{userID}")
    suspend fun addUserToEvent(@Path("eventID") eventID: Int, @Path("userID") userID: Int)
}