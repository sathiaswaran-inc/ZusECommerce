package com.example.zussathia.utils

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthInterceptor : Interceptor, KoinComponent {

    val prefUtils: PrefUtils by inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefUtils.getValueString(SESSION_TOKEN)
        val request = chain.request()
            .newBuilder()
            .apply {
                if (!token.isNullOrEmpty()) {
                    addHeader("token", token)
                }
            }.build()
        return chain.proceed(request)
    }
}