package com.example.cameraapp

import android.app.Application
import com.example.cameraapp.Components.TTSManager
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseService.firebaseAuth = FirebaseAuth.getInstance()
        FirebaseService.oneTapClient = Identity.getSignInClient(this)
        FirebaseService.signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
        TTSManager.init(this)
    }
}