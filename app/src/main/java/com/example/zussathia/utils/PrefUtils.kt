package com.example.zussathia.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PrefUtils(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(GLOBAL_VALUE, Context.MODE_PRIVATE)

    fun save(keyName: String, value: String) {
        preferences.edit {
            putString(keyName, value)
        }
    }

    fun getValueString(keyName: String): String? {
        return preferences.getString(keyName, null)
    }


}