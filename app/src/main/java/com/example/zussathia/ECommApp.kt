package com.example.zussathia

import android.app.Application
import com.example.zussathia.di.appModule
import com.example.zussathia.di.repoModule
import com.example.zussathia.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class ECommApp: Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@ECommApp)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}