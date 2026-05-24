package com.example.zussathia.data.model

data class UpdateCartResponse(
    val status: String,
    val numOfCartItems: Int,
    val data: CartData
)
