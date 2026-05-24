package com.example.zussathia.utils

import androidx.lifecycle.Observer

class EventObserver<T>(private val onEventUnHandledContent: (T) -> Unit): Observer<LiveDataEvent<T>> {
    override fun onChanged(value: LiveDataEvent<T>) {
        value.getContentIfNotHandled()?.let { value ->
            onEventUnHandledContent(value)
        }
    }

}