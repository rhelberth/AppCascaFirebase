package com.example.appcascafirebase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Outras inicializações globais podem vir aqui, se necessário
    }
}