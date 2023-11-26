package com.example.explorecity.api.callers

import com.example.explorecity.api.classes.auth.LoginValidResponse
import com.example.explorecity.api.classes.auth.RegistrationBody
import com.example.explorecity.api.classes.auth.RegistrationResponse
import com.example.explorecity.api.classes.event.EventBody
import com.example.explorecity.api.classes.event.EventDetailBody
import com.example.explorecity.api.classes.event.EventListResponse
import com.example.explorecity.api.classes.event.Location
import com.example.explorecity.api.classes.event.SingleEventResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterfaces {
    @GET("/auth")
    suspend fun validateLogin(): LoginValidResponse

    @POST("/auth")
    fun registerUser(@Body registrationBody: RegistrationBody): Call<RegistrationResponse>

    @GET("/event")
    suspend fun getUserEvents(): EventListResponse

    @POST("/event")
    suspend fun createNewEvent(@Body eventBody: EventBody): RegistrationResponse

    @DELETE("/event/{eventID}")
    suspend fun deleteEvent(@Path("eventID") eventID: Int): Call<Void>

    @GET("/event/{id}")
    suspend fun getSingleEvent(@Path("id") id: Int): SingleEventResponse

    @PUT("/event/{eventID}/user/{userID}")
    suspend fun addUserToEvent(@Path("eventID") eventID: Int, @Path("userID") userID: Int)

    @DELETE("/event/{eventID}/user/{userID}")
    suspend fun removeUserFromEvent(@Path("eventID") eventID: Int, @Path("userID") userID: Int)

    @POST("/location/event/{eventID}")
    suspend fun updateUserLocationByEventID(@Body coordinates: Location, @Path("eventID") eventID: Int)

    @GET("/event/search")
    suspend fun searchFilterQuery(
        @Query("query") query: String?,
        @Query("ey") startYear: Int?,
        @Query("em") startMonth: Int?,
        @Query("ed") startDay: Int?,
        @Query("ly") endYear: Int?,
        @Query("lm") endMonth: Int?,
        @Query("ld") endDay: Int?,
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("radius") radius: Int?
    ): List<EventDetailBody>
}