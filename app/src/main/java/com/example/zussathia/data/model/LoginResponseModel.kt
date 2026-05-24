package com.example.zussathia.data.model

data class LoginResponseModel(
    val message: String,
    val token: String,
    val user: UserModel
)