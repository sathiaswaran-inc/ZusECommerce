package com.example.zussathia.di

import com.example.zussathia.ui.StoreViewModel
import com.example.zussathia.ui.splash.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        AuthViewModel(get())
    }

    viewModel {
        StoreViewModel(get())
    }
}