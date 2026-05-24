package com.example.zussathia.data.model

import com.google.gson.Gson
import com.google.gson.JsonElement

data class CartProduct(
    val count: Int,
    val _id: String,
    val product: JsonElement,
    val price: Int
) {
    val productId: String
        get() = when {
            product.isJsonPrimitive -> product.asString
            product.isJsonObject -> {
                val obj = product.asJsonObject
                when {
                    obj.has("_id") -> obj.get("_id").asString
                    obj.has("id") -> obj.get("id").asString
                    else -> ""
                }
            }
            else -> ""
        }

    fun getProductDetails(): ProductData? {
        return if (product.isJsonObject) {
            Gson().fromJson(product, ProductData::class.java)
        } else null
    }
}
