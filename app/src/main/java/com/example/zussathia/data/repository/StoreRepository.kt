package com.example.zussathia.data.repository

import com.example.zussathia.data.api.ApiHelper
import com.example.zussathia.data.model.AddCartModel
import com.example.zussathia.data.model.ProductIDModel
import com.example.zussathia.data.model.UpdateCartRequest

class StoreRepository(private val apiHelper: ApiHelper) {

    suspend fun getStoreCategory() =
        apiHelper.category()

    suspend fun getStoreProducts() =
        apiHelper.product()

    suspend fun addToFav(productIDModel: ProductIDModel) =
        apiHelper.addToWishList(productIDModel)

    suspend fun getAllFavItems() =
        apiHelper.getWishLists()

    suspend fun deleteFromFav(productID: String) =
        apiHelper.deleteFromWishList(productID)

    suspend fun addToCart(productIDModel: ProductIDModel) =
        apiHelper.addToCart(productIDModel)

    suspend fun getCart() =
        apiHelper.getCart()

    suspend fun updateCartQuantity(productId: String, updateCartRequest: UpdateCartRequest) =
        apiHelper.updateCartQuantity(productId, updateCartRequest)

    suspend fun removeCartItem(productId: String) =
        apiHelper.removeCartItem(productId)

    suspend fun clearCart() =
        apiHelper.clearCart()
}