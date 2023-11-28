package com.example.explorecity.api.models

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorecity.api.callers.RetrofitInstance
import com.example.explorecity.api.classes.RecommendationResponseBody
import com.example.explorecity.api.classes.auth.LoginValidResponse
import com.example.explorecity.api.classes.auth.RegistrationBody
import com.example.explorecity.api.classes.auth.RegistrationErrorResponse
import com.example.explorecity.api.classes.auth.RegistrationResponse
import com.example.explorecity.api.classes.chat.ChatMessage
import com.example.explorecity.api.classes.chat.ChatMessageBody
import com.example.explorecity.api.classes.chat.Message
import com.example.explorecity.api.classes.chat.TimeStamp
import com.example.explorecity.api.classes.event.DateTimeBody
import com.example.explorecity.api.classes.event.EventBody
import com.example.explorecity.api.classes.event.EventDetailBody
import com.example.explorecity.api.classes.event.EventFilter
import com.example.explorecity.api.classes.event.Location
import com.example.explorecity.api.classes.event.SingleEventResponse
import com.example.explorecity.api.classes.event.emptySingleEventResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.TimeZone

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
//        Log.d("REGISTRATION", listOfErrors[1].description)
//        Log.d("REGISTRATION", "$userID")
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

    suspend fun isLoginValid(): LoginValidResponse {
        return try {
            RetrofitInstance.authenticateUser().validateLogin()
        } catch (e: Exception) {
            LoginValidResponse(-1, "null")
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

    suspend fun deleteEvent(eventID: Int, hostID: Int): String { // returns a message from the call
        if (hostID != UserInformation.instance.getUserID()) {
            return "User cannot delete event, event not deleted"
        }
        try {
            RetrofitInstance.authenticateUser().deleteEvent(eventID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Deletion Successful"
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

    fun explorePageFilter(): EventFilter {
        val currentTime: DateTimeBody = getCurrentDate()
        val weekFromNow: DateTimeBody = getCurrentDate(7)
        val userLocation = UserInformation.instance.getUserLocation()

        return EventFilter(
            query = null,
            startDate = currentTime,
            endDate = weekFromNow,
            userLocation = userLocation,
            radius = 50
        )
    }

    fun recommendationsFilter(query:String, location: Location?): EventFilter {
        return EventFilter(
            query = query,
            startDate = DateTimeBody(0, 0, 0, 0, 0),
            endDate = DateTimeBody(0, 0, 0, 0, 0),
            userLocation = location ?: UserInformation.instance.getUserLocation(),
            radius = 10
        )
    }

    suspend fun getListOfRecommendations(filter: EventFilter): List<RecommendationResponseBody> {
        return try {
            RetrofitInstance.authenticateUser().getRecommendations(
                query = filter.query,
                latitude = filter.userLocation.lat,
                longitude = filter.userLocation.lon,
                radius = filter.radius
            )
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchChatMessages(eventID: Int, msgList: MutableList<Message>): String {
        // Clear the current msgList:
        msgList.clear()
        var errorMsg = "No messages in chat yet, be the first one!"
//        try {
//            val chatList = RetrofitInstance.authenticateUser().getChatMessages(eventID)
//            addMessagesToList(response = chatList, currentList = msgList)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            errorMsg = "Oops, something went wrong"
//        }

        val call = RetrofitInstance.authenticateUser().getChatMessages(eventID)
        Log.d("MSG", "Event ID: $eventID")
        call.enqueue(object: Callback<List<ChatMessage>> {
            override fun onResponse(
                call: Call<List<ChatMessage>>,
                response: Response<List<ChatMessage>>
            ) {
                Log.d("MSG", "Response Code: ${response.code()}")
                when (response.code()) {
                    200 -> {
                        addMessagesToList(response = response.body(), currentList = msgList)
                        Log.d("MSG", response.body().toString())
                    }
                    401 -> errorMsg = "User not logged in"
                    404 -> errorMsg = "Event does not exist"
                    403 -> errorMsg = "User not authorized to send messages in this chat"
                    else -> {
                        Log.e("MSG", "Unknown response")
                        errorMsg = "Unknown Error, please try again later"
                    }
                }
            }
            override fun onFailure(call: Call<List<ChatMessage>>, t: Throwable) {
                errorMsg = "Unknown Error, please try again later"
            }
        }
        )
        return errorMsg
    }

    private fun addMessagesToList(response: List<ChatMessage>?, currentList: MutableList<Message>) {
        if (response != null) {
            for (msg in response) {
                currentList.add(
                    Message(
                        userName = msg.sender.displayname,
                        content = msg.text,
                        timestamp = convertToStringDateTime(msg.time)
                    )
                )
                Log.d("MSG", "added response: $msg")
            }
        }
        if (response == null) {
            Log.d("MSG", "response was null!")
        }
    }

    fun postChatMessage(eventID: Int, message: String): Pair<Boolean, String> {
        var msg = ""
        var success = false
        val call = RetrofitInstance.authenticateUser().postChatMessage(eventID, ChatMessageBody(text = message))
        call.enqueue(object: Callback<TimeStamp> {
            override fun onResponse(call: Call<TimeStamp>, response: Response<TimeStamp>) {
                msg = when (response.code()) {
                    200  -> {
                        success = true
                        "Successful message at: ${convertToStringDateTime(response.body()!!.time)}"
                    }
                    401  -> "User is not logged in"
                    415  -> "Bad Request, contact support"
                    400  -> "Bad Format, contact support"
                    422  -> parseMessagePostingErrors(response.errorBody()!!.string())
                    404  -> "Event doesn't exist"
                    403  -> "User cannot post messages to this chat"
                    500  -> "Internal server error, contact support"
                    else -> "Unknown Error occurred, please try again"
                }
            }

            override fun onFailure(call: Call<TimeStamp>, t: Throwable) {
                msg = "Unknown Error occurred, please try again"
            }
        }
        )

        return Pair(success, msg)
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertToStringDateTime(unixTime: Double): String {
        // Convert Unix timestamp to milliseconds
        val timeInMillis: Double = kotlin.math.ceil(unixTime / 1000L)

        // Create a Date object using the milliseconds
        val date = Date(timeInMillis.toLong())

        // Create a SimpleDateFormat object with the desired format and set the timezone
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm")
        sdf.timeZone = TimeZone.getDefault() // You can set a specific timezone if needed

        // Format the date and return the result
        return sdf.format(date)
    }

    private fun parseMessagePostingErrors(errorJSON: String): String {
        var errorMsg = ""
        try {
            val jsonBody = JSONObject(errorJSON)
            errorMsg = jsonBody.optString("description")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return errorMsg
    }

    fun getCurrentDate(daysAdded: Int = 0): DateTimeBody {
        var currentDateTime = LocalDateTime.now()
        currentDateTime = currentDateTime.plusDays(daysAdded.toLong())

        return DateTimeBody(
            day = currentDateTime.dayOfMonth,
            hour = currentDateTime.hour,
            minute = currentDateTime.minute,
            month = currentDateTime.monthValue,
            year = currentDateTime.year
        )
    }
}