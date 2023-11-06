package com.example.explorecity.api.callers

import android.util.Log
import com.example.explorecity.api.classes.LoginValidResponse
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

    val loginService: ApiInterfaces by lazy {
        baseRequestWithAuthentication().create(ApiInterfaces::class.java)
    }

    fun authenticateUser(): ApiInterfaces {
        return baseRequestWithAuthentication().create(ApiInterfaces::class.java)
    }

    fun registerService(): ApiInterfaces {
        return baseRequestWithoutAuthentication().create(ApiInterfaces::class.java)
    }
}