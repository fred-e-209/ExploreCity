package com.example.explorecity.api.callers

import com.example.explorecity.api.models.UserInformation
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val apiInstance = UserInformation.instance

    private fun baseRequestWithAuthentication(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor(apiInstance.getUsername(), apiInstance.getPassword()))
            .build()
        return Retrofit.Builder()
            .baseUrl("https://api.explorecityapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    private fun baseRequestWithoutAuthentication(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.explorecityapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}