package com.example.zussathia.data.api

import com.example.zussathia.data.model.CategoryModel
import com.example.zussathia.data.model.LoginModel
import com.example.zussathia.data.model.LoginResponseModel
import com.example.zussathia.data.model.ProductModel
import com.example.zussathia.data.model.AddCartModel
import com.example.zussathia.data.model.AddDelFromFavModel
import com.example.zussathia.data.model.ProductIDModel
import com.example.zussathia.data.model.UpdateCartRequest
import com.example.zussathia.data.model.UpdateCartResponse
import com.example.zussathia.data.model.WishListModel

interface ApiHelper {
    suspend fun autoAuth(loginModel: LoginModel): LoginResponseModel
    suspend fun category(): CategoryModel
    suspend fun product(): ProductModel
    suspend fun addToWishList(productIDModel: ProductIDModel): AddDelFromFavModel
    suspend fun getWishLists(): WishListModel
    suspend fun deleteFromWishList(productID: String): AddDelFromFavModel
    suspend fun addToCart(productIDModel: ProductIDModel): AddCartModel
    suspend fun getCart(): AddCartModel
    suspend fun updateCartQuantity(productId: String, updateCartRequest: UpdateCartRequest): UpdateCartResponse
    suspend fun removeCartItem(productId: String): AddCartModel
    suspend fun clearCart(): AddCartModel
}