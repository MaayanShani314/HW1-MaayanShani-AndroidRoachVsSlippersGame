package com.example.progect1_game.utilities

import android.content.Context

class SharedPreferencesManager private constructor(context: Context) {

    private val sp = context.getSharedPreferences(
        Constants.SP_KEYS.FILE_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        @Volatile
        private var instance: SharedPreferencesManager? = null

        fun init(context: Context): SharedPreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManager {
            return instance
                ?: throw IllegalStateException("SharedPreferencesManager not initialized")
        }
    }

    fun putString(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sp.getString(key, defaultValue) ?: defaultValue
    }
}

