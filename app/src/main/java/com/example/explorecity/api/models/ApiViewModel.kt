package com.example.explorecity.api.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorecity.api.callers.RetrofitInstance
import com.example.explorecity.api.classes.auth.LoginValidResponse
import com.example.explorecity.api.classes.auth.RegistrationBody
import com.example.explorecity.api.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.explorecity.api.classes.auth.RegistrationErrorResponse
import com.example.explorecity.api.classes.auth.RegistrationResponse
import com.example.explorecity.api.classes.event.EventBody
import com.example.explorecity.api.classes.event.Hosting
import com.example.explorecity.api.classes.event.SingleEventResponse
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONException

class ApiViewModel: ViewModel() {
    private val _events = MutableLiveData<List<Hosting>>()
    val userEvents: LiveData<List<Hosting>> = _events

    private val _singleEvent = MutableLiveData<SingleEventResponse>()
    val singleEvent: LiveData<SingleEventResponse> = _singleEvent

    suspend fun createNewAccount(user: RegistrationBody): Pair<Int, List<RegistrationErrorResponse>> {
//        return try {
//            val newUser = RetrofitInstance.registerService().registerUser(user) // Repository call
//            newUser.id
//        } catch (e: Exception) {
//            Log.e("API Fault", e.printStackTrace().toString())
//            -1 // notify front end that the creation failed.
//        }
        // The below code is an attempt to include error messages into the return.
        val service = RetrofitInstance.registerService()
        val call = service.registerUser(user)
        var userID: Int = -1
        var listOfErrors: List<RegistrationErrorResponse> = emptyList()
        call.enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.errorBody() != null && response.code() == 422) {
                    Log.d("REGISTRATION", "body is not good")
                    try {
                        listOfErrors = parseRegistrationErrors(response.errorBody()!!.string())
                    } catch (e: Exception) {
                        Log.e("REGISTRATION", "Error while parsing")
                    }
                } else if (response.code() == 200) {
                    Log.d("REGISTRATION", "body is good")
                    userID = response.body()?.id!!
                } else  {
                    Log.e("REGISTRATION", "Error under correct response")
                }
            }
            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Log.e("REGISTRATION", t.message.toString())
            }
        })
        delay(1500)
        Log.d("REGISTRATION", listOfErrors[1].description)
        Log.d("REGISTRATION", "$userID")
        return Pair(userID, listOfErrors)
    }

    fun parseRegistrationErrors(errorJSON: String): List<RegistrationErrorResponse> {
        val myList = mutableListOf<RegistrationErrorResponse>()
        try {
            val jsonArray = JSONArray(errorJSON)
            Log.d("REGISTRATION", "Length: ${jsonArray.length()}, body: $errorJSON")
            for (i in 0 until jsonArray.length()) {
                val errorObject = jsonArray.getJSONObject(i)
                val description = errorObject.optString("description")
                Log.d("REGISTRATION", description)
                val field = errorObject.optString("field")
                Log.d("REGISTRATION", field)
                myList.add(RegistrationErrorResponse(description = description, field = field))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return myList
    }

    suspend fun isLoginValid(): Int {
        return try {
            val userAuth = RetrofitInstance.authenticateUser().validateLogin()
            userAuth.id
        } catch (e: Exception) {
            -1
        }
    }

    suspend fun getUserEvents(): List<Hosting> {
        return try {
            val userEvents = RetrofitInstance.authenticateUser().getUserEvents()
            userEvents.hosting
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchUserEvents() {
        viewModelScope.launch {
            try {
                val userEvents = RetrofitInstance.authenticateUser().getUserEvents()
                _events.value = userEvents.hosting
            } catch (e: Exception) {
                _events.value = emptyList()
            }
        }
    }

    suspend fun createEvent(eventBody: EventBody): Int {
        return try {
            val event = RetrofitInstance.authenticateUser().createNewEvent(eventBody)
            event.id
        } catch (e: Exception) {
            -1
        }
    }

    fun fetchEvent(eventID: Int) {
        viewModelScope.launch {
            try {
                val event = RetrofitInstance.authenticateUser().getSingleEvent(eventID)
                _singleEvent.value = event
            } catch (e: Exception) {
                throw e
            }
        }
    }
}