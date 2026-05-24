package com.example.zussathia.data.api

import com.example.zussathia.data.model.AddCartModel
import com.example.zussathia.data.model.AddDelFromFavModel
import com.example.zussathia.data.model.CategoryModel
import com.example.zussathia.data.model.LoginModel
import com.example.zussathia.data.model.LoginResponseModel
import com.example.zussathia.data.model.ProductIDModel
import com.example.zussathia.data.model.ProductModel
import com.example.zussathia.data.model.UpdateCartRequest
import com.example.zussathia.data.model.UpdateCartResponse
import com.example.zussathia.data.model.WishListModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("auth/signin")
    suspend fun logIn(
        @Body loginModel: LoginModel
    ): LoginResponseModel

    @GET("categories")
    suspend fun categories(): CategoryModel

    @GET("products")
    suspend fun products(): ProductModel

    @POST("wishlist")
    suspend fun addToWishLists(
        @Body productIDModel: ProductIDModel
    ): AddDelFromFavModel

    @GET("wishlist")
    suspend fun getWishList(): WishListModel

    @DELETE("wishlist/{productID}")
    suspend fun deleteFromWishLists(
        @Path("productID") id: String
    ): AddDelFromFavModel

    @POST("cart")
    suspend fun addToCart(
        @Body productIDModel: ProductIDModel
    ): AddCartModel

    @GET("cart")
    suspend fun getCart(): AddCartModel

    @PUT("cart/{productId}")
    suspend fun updateCartQuantity(
        @Path("productId") productId: String,
        @Body updateCartRequest: UpdateCartRequest
    ): UpdateCartResponse

    @DELETE("cart/{productId}")
    suspend fun removeCartItem(
        @Path("productId") productId: String
    ): AddCartModel

    @DELETE("cart")
    suspend fun clearCart(): AddCartModel
}