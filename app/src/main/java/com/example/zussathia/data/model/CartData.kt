package com.example.zussathia.data.model

data class CartData(
    val _id: String,
    val cartOwner: String,
    val products: List<CartProduct>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val totalCartPrice: Int
)