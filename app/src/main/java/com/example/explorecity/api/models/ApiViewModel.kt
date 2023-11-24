package com.example.explorecity.api.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorecity.api.callers.RetrofitInstance
import com.example.explorecity.api.classes.auth.RegistrationBody
import com.example.explorecity.api.classes.auth.RegistrationErrorResponse
import com.example.explorecity.api.classes.auth.RegistrationResponse
import com.example.explorecity.api.classes.event.DateTimeBody
import com.example.explorecity.api.classes.event.EventBody
import com.example.explorecity.api.classes.event.EventDetailBody
import com.example.explorecity.api.classes.event.EventFilter
import com.example.explorecity.api.classes.event.SingleEventResponse
import com.example.explorecity.api.classes.event.emptySingleEventResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class ApiViewModel: ViewModel() {
    private val _events = MutableLiveData<List<EventDetailBody>>()
    val userEvents: LiveData<List<EventDetailBody>> = _events

    private val _searchEvents = MutableLiveData<List<EventDetailBody>>()
    val searchEvents: LiveData<List<EventDetailBody>> = _searchEvents

    private val _singleEvent = MutableLiveData<SingleEventResponse>()
    val singleEvent: LiveData<SingleEventResponse> = _singleEvent

    suspend fun sendUpdatedLocation() {
        val eventsToday: List<Int> = UserInformation.instance.getUserEventIDs()
        for (eventID in eventsToday) {
            postUserLocationByEventID(eventID = eventID)
        }
    }

    private suspend fun postUserLocationByEventID(eventID: Int) {
        val userLocation = UserInformation.instance.getUserLocation()
        try {
            Log.d("LOCATION_UPDATE", "event id being used: $eventID")
            RetrofitInstance.authenticateUser().updateUserLocationByEventID(coordinates = userLocation, eventID = eventID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateEventsToday(events: List<EventDetailBody>) {
        val userEventsList = UserInformation.instance
        userEventsList.clearUserEventIDs()
        for (event in events) {
            val isValid: Boolean = isBetweenDates(startTime = event.start, endTime = event.end, daysAdded = 1)
            if (isValid) {
                userEventsList.addToUserEventIDs(event.id)
            }
        }
    }

    fun isBetweenDates(startTime: DateTimeBody, currentTime: DateTimeBody? = null, endTime: DateTimeBody, daysAdded: Int? = null): Boolean {
        val days = daysAdded ?: 0
        val now = if (currentTime == null) LocalDate.now() else convertToLocalDate(currentTime)
        val start = convertToLocalDate(startTime, daysAdded = -days)
        val end = convertToLocalDate(endTime, daysAdded = days)
        return (start.isBefore(now) && end.isAfter(now))
    }

    fun convertToLocalDate(date: DateTimeBody, daysAdded: Int? = null): LocalDate {
        val days = daysAdded ?: 0
        return LocalDate.of(date.year, date.month, date.day + days)
    }

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


    fun fetchUserEvents() {
        viewModelScope.launch {
            try {
                val userEvents = RetrofitInstance.authenticateUser().getUserEvents()
                _events.value = userEvents.attending
            } catch (e: Exception) {
                _events.value = emptyList()
            }
        }
    }

    fun fetchHostEvents() {
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
                Log.d("DETAILS", event.displayname)
                _singleEvent.value = event
                Log.d("DETAILS", event.description)
            } catch (e: Exception) {
                _singleEvent.value = emptySingleEventResponse()
            }
        }
    }

    suspend fun addUserToEvent(eventID: Int, userID: Int) {
        return try {
            RetrofitInstance.authenticateUser().addUserToEvent(eventID = eventID, userID = userID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun removeUserFromEvent(eventID: Int, userID: Int) {
        return try {
            RetrofitInstance.authenticateUser().removeUserFromEvent(eventID = eventID, userID = userID)
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    suspend fun fetchEventsByFilter(eventFilter: EventFilter): List<EventDetailBody> {
        try {
            return RetrofitInstance.authenticateUser().searchFilterQuery(
                query = if (eventFilter.query == "") null else eventFilter.query,
                startDay = eventFilter.startDate.day,
                startMonth = eventFilter.startDate.month,
                startYear = eventFilter.startDate.year,
                endDay = eventFilter.endDate.day,
                endMonth = eventFilter.endDate.month,
                endYear = eventFilter.endDate.year,
                latitude = eventFilter.userLocation.lat,
                longitude = eventFilter.userLocation.lon,
                radius = eventFilter.radius
            )
        } catch (e: Exception) {
            throw e
        }
    }
}