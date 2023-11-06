package com.example.explorecity.api.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorecity.api.callers.RetrofitInstance
import com.example.explorecity.api.classes.LoginValidResponse
import com.example.explorecity.api.classes.RegistrationBody
import com.example.explorecity.api.classes.RegistrationErrorResponse
import com.example.explorecity.api.classes.RegistrationResponse
import com.example.explorecity.api.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    suspend fun createNewAccount(user: RegistrationBody): Int {
        return try {
            val newUser = RetrofitInstance.registerService().registerUser(user) // Repository call
            newUser.id
        } catch (e: Exception) {
            Log.e("API Fault", e.printStackTrace().toString())
            -1 // notify front end that the creation failed.
        }
        // The below code is an attempt to include error messages into the return.
//        val service = RetrofitInstance.registerService()
//        val call = service.registerUser(user)
//        var userID: Int = -1
//        call.enqueue(object : Callback<RegistrationResponse> {
//            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
//                if (response.code() == 200) {
//                    userID = response.body()?.id!!
//                }
//            }
//            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
//                val responseList: String? = t.message
//            }
//        })
//        if (userID > 0) {
//            return Pair(userID, emptyList())
//        } else {
//            return Pair(-1, )
//        }
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