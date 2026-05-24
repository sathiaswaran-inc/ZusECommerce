package com.example.zussathia.data.api

import com.example.zussathia.data.model.CategoryModel
import com.example.zussathia.data.model.LoginModel
import com.example.zussathia.data.model.LoginResponseModel
import com.example.zussathia.data.model.AddCartModel
import com.example.zussathia.data.model.AddDelFromFavModel
import com.example.zussathia.data.model.ProductIDModel
import com.example.zussathia.data.model.ProductModel
import com.example.zussathia.data.model.UpdateCartRequest
import com.example.zussathia.data.model.UpdateCartResponse
import com.example.zussathia.data.model.WishListModel

class ApiHelperImpl(private val apiService: ApiService): ApiHelper {

    override suspend fun autoAuth(loginModel: LoginModel): LoginResponseModel =
        apiService.logIn(loginModel)

    override suspend fun category(): CategoryModel  =
        apiService.categories()

    override suspend fun product(): ProductModel =
        apiService.products()

    override suspend fun addToWishList(productIDModel: ProductIDModel): AddDelFromFavModel =
        apiService.addToWishLists(productIDModel)

    override suspend fun getWishLists(): WishListModel =
        apiService.getWishList()

    override suspend fun deleteFromWishList(productID: String): AddDelFromFavModel =
        apiService.deleteFromWishLists(productID)

    override suspend fun addToCart(productIDModel: ProductIDModel): AddCartModel =
        apiService.addToCart(productIDModel)

    override suspend fun getCart(): AddCartModel =
        apiService.getCart()

    override suspend fun updateCartQuantity(
        productId: String,
        updateCartRequest: UpdateCartRequest
    ): UpdateCartResponse =
        apiService.updateCartQuantity(productId, updateCartRequest)

    override suspend fun removeCartItem(productId: String): AddCartModel =
        apiService.removeCartItem(productId)

    override suspend fun clearCart(): AddCartModel =
        apiService.clearCart()
}