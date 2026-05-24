package com.example.zussathia.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zussathia.data.model.LoginModel
import com.example.zussathia.data.model.LoginResponseModel
import com.example.zussathia.data.repository.AuthRepository
import com.example.zussathia.utils.LiveDataEvent
import com.example.zussathia.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {

    private val _autoSignIn: MutableLiveData<LiveDataEvent<Resource<LoginResponseModel>>>
        by lazy { MutableLiveData<LiveDataEvent<Resource<LoginResponseModel>>>() }
    val autoSignIn: LiveData<LiveDataEvent<Resource<LoginResponseModel>>>
        get() = _autoSignIn

    fun autoSignIn() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _autoSignIn.postValue(LiveDataEvent(Resource.loading(null)))
                val login = authRepository.autoSignInUser(
                    LoginModel(
                        email = "sathia@zus.com",
                        password = "Sathia@123"
                    )
                )
                _autoSignIn.postValue(LiveDataEvent(Resource.success(login)))
            } catch (ex: Exception) {
                _autoSignIn.postValue(LiveDataEvent(Resource.error(ex.message ?: "", null)))
            }
        }
    }
}