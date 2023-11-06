package com.example.explorecity.api.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorecity.api.callers.RetrofitInstance
import com.example.explorecity.api.classes.LoginValidResponse
import com.example.explorecity.api.classes.RegistrationBody
import com.example.explorecity.api.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.explorecity.api.classes.RegistrationErrorResponse
import com.example.explorecity.api.classes.RegistrationResponse
import org.json.JSONArray
import org.json.JSONException

class ApiViewModel: ViewModel() {
    private val repository = Repository()

    private val _loginResponse = MutableLiveData<LoginValidResponse>()
    val loginResponse: LiveData<LoginValidResponse> = _loginResponse

    fun fetchLoginResponse() {
        viewModelScope.launch {
            try {
                val response = repository.validateLogin()
                _loginResponse.value = response
            } catch (e: Exception) {
                _loginResponse.value = LoginValidResponse(id = -1, verified = false)
            }
        }
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
                        listOfErrors = parseRegistrationErrors(response.errorBody().toString())
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
        return if (userID > 0) {
            Pair(userID, emptyList())
        } else {
            Pair(-1, listOfErrors)
        }
    }

    fun parseRegistrationErrors(errorJSON: String): List<RegistrationErrorResponse> {
        val myList = mutableListOf<RegistrationErrorResponse>()
        try {
            val jsonArray = JSONArray(errorJSON)
            for (i in 0 until jsonArray.length()) {
                val errorObject = jsonArray.getJSONObject(i)
                val description = errorObject.optString("description")
                val field = errorObject.optString("field")
                myList.add(RegistrationErrorResponse(description, field))
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
}