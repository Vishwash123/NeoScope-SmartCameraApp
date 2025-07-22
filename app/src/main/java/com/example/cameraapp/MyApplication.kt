package com.example.cameraapp

import android.app.Application
import com.example.cameraapp.Components.TTSManager
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseService.firebaseAuth = FirebaseAuth.getInstance()
        TTSManager.init(this)
    }
}