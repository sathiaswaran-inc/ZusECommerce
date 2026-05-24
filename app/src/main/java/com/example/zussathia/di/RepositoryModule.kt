package com.example.zussathia.di

import com.example.zussathia.data.repository.AuthRepository
import com.example.zussathia.data.repository.StoreRepository
import org.koin.dsl.module

val repoModule = module {

    single {
        AuthRepository(get())
    }

    single {
        StoreRepository(get())
    }
}