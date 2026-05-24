package com.example.zussathia.data.repository

import com.example.zussathia.data.api.ApiHelper
import com.example.zussathia.data.model.LoginModel

class AuthRepository(private val apiHelper: ApiHelper) {

    suspend fun autoSignInUser(loginModel: LoginModel) =
        apiHelper.autoAuth(loginModel)
}