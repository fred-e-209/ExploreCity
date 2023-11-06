package com.example.explorecity.api.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorecity.api.classes.LoginValidResponse
import com.example.explorecity.api.repository.Repository
import kotlinx.coroutines.launch

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
}