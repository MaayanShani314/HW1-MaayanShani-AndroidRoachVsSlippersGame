package com.example.progect1_game

import android.app.Application
import com.example.progect1_game.utilities.SharedPreferencesManager
import com.example.progect1_game.utilities.SignalManager


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        SignalManager.init(this)
    }
}