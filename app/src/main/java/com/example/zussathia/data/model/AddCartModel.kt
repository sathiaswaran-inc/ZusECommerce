package com.example.zussathia.data.model

data class AddCartModel(
    val status: String,
    val message: String,
    val numOfCartItems: Int,
    val cartId: String? = null,
    val data: CartData? = null
)