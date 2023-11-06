package com.example.explorecity.api.repository

import com.example.explorecity.api.callers.RetrofitInstance
import com.example.explorecity.api.classes.LoginValidResponse

class Repository {
    suspend fun validateLogin(): LoginValidResponse {
        return RetrofitInstance.loginService.validateLogin()
    }
}