package com.example.zussathia.ui

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zussathia.data.model.AddCartModel
import com.example.zussathia.data.model.AddDelFromFavModel
import com.example.zussathia.data.model.CategoryModel
import com.example.zussathia.data.model.ProductIDModel
import com.example.zussathia.data.model.ProductModel
import com.example.zussathia.data.model.UpdateCartRequest
import com.example.zussathia.data.model.WishListModel
import com.example.zussathia.data.repository.StoreRepository
import com.example.zussathia.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoreViewModel(private val storeRepository: StoreRepository): ViewModel() {

    private val _storeCategories = MutableLiveData<Resource<CategoryModel>>()
    val storeCategories: LiveData<Resource<CategoryModel>> = _storeCategories

    private val _storeProducts = MutableLiveData<Resource<ProductModel>>()
    val storeProducts: LiveData<Resource<ProductModel>> = _storeProducts

    private val _addToFav = MutableLiveData<Resource<AddDelFromFavModel>>()
    val addToFav: LiveData<Resource<AddDelFromFavModel>> = _addToFav

    private val _getFavItem = MutableLiveData<Resource<WishListModel>>()
    val getFavItem: LiveData<Resource<WishListModel>> = _getFavItem

    private val _deleteFavItem = MutableLiveData<Resource<AddDelFromFavModel>>()
    val deleteFavItem: LiveData<Resource<AddDelFromFavModel>> = _deleteFavItem

    private val _addToCart = MutableLiveData<Resource<AddCartModel>>()
    val addToCart: LiveData<Resource<AddCartModel>> = _addToCart

    private val _cartItemCount = MutableLiveData<Int>(0)
    val cartItemCount: LiveData<Int> = _cartItemCount

    private val _cartData = MutableLiveData<Resource<AddCartModel>>()
    val cartData: LiveData<Resource<AddCartModel>> = _cartData

    val favProducts = mutableStateMapOf<String, Boolean>()

    fun getAllCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _storeCategories.postValue(Resource.loading(null))
                val cats = storeRepository.getStoreCategory()
                _storeCategories.postValue(Resource.success(cats))
            } catch (ex: Exception) {
                _storeCategories.postValue(Resource.error(ex.message ?: "Unknown Error", null))
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _storeProducts.postValue(Resource.loading(null))
                val products = storeRepository.getStoreProducts()
                _storeProducts.postValue(Resource.success(products))
            } catch (ex: Exception) {
                _storeProducts.postValue(Resource.error(ex.message ?: "Unknown Error", null))
            }
        }
    }

    fun addItemToWishList(productID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _addToFav.postValue(Resource.loading(null))
                val productIDModel = ProductIDModel(productId = productID)
                val addedFavItem = storeRepository.addToFav(productIDModel)
                _addToFav.postValue(Resource.success(addedFavItem))
                if (addedFavItem.status == "success") {
                    favProducts[productID] = true
                    getAllFavItem()
                }
            } catch (ex: Exception) {
                _addToFav.postValue(Resource.error(ex.message ?: "Unknown Error", null))
            }
        }
    }

    fun deleteItemFromWishList(productID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _deleteFavItem.postValue(Resource.loading(null))
                val deleteFavItem = storeRepository.deleteFromFav(productID)
                _deleteFavItem.postValue(Resource.success(deleteFavItem))
                if (deleteFavItem.status == "success") {
                    favProducts[productID] = false
                    getAllFavItem()
                }
            } catch (ex: Exception) {
                _deleteFavItem.postValue(Resource.error(ex.message ?: "Unknown Error", null))
            }
        }
    }

    fun getAllFavItem() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _getFavItem.postValue(Resource.loading(null))
                val favs = storeRepository.getAllFavItems()
                _getFavItem.postValue(Resource.success(favs))
                favs.data.forEach{ productData ->
                    favProducts[productData._id] = true
                }
            } catch (ex: Exception) {
                _getFavItem.postValue(Resource.error(ex.message ?: "Unknown Error", null))
            }
        }
    }

    fun addProductToCart(productID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _addToCart.postValue(Resource.loading(null))
                val productIDModel = ProductIDModel(productId = productID)
                val response = storeRepository.addToCart(productIDModel)
                _addToCart.postValue(Resource.success(response))
                if (response.status == "success") {
                    val totalQuantity = response.data?.products?.sumOf { it.count } ?: response.numOfCartItems
                    _cartItemCount.postValue(totalQuantity)
                }
            } catch (ex: Exception) {
                _addToCart.postValue(Resource.error(ex.message ?: "Unknown Error", null))
            }
        }
    }

    fun getCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _cartData.postValue(Resource.loading(null))
                val response = storeRepository.getCart()
                if (response.status == "success") {
                    val totalQuantity = response.data?.products?.sumOf { it.count } ?: response.numOfCartItems
                    _cartItemCount.postValue(totalQuantity)
                    _cartData.postValue(Resource.success(response))
                }
            } catch (ex: Exception) {
                _cartData.postValue(Resource.error(ex.message ?: "Unknown Error", null))
            }
        }
    }

    fun updateCartQuantity(productId: String, count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = storeRepository.updateCartQuantity(productId, UpdateCartRequest(count))
                if (response.status == "success") {
                    getCartItems()
                }
            } catch (ex: Exception) {
                // Handle error
            }
        }
    }

    fun removeCartItem(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = storeRepository.removeCartItem(productId)
                if (response.status == "success") {
                    getCartItems()
                }
            } catch (ex: Exception) {
                // Handle error
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = storeRepository.clearCart()
                if (response.status == "success") {
                    getCartItems()
                }
            } catch (ex: Exception) {
                // Handle error
            }
        }
    }

    fun resetAddToCartState() {
        _addToCart.value = null
    }
}