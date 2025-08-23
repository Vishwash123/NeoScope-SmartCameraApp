package com.example.cameraapp

import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth

object FirebaseService {
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var oneTapClient:SignInClient
    lateinit var signInRequest: BeginSignInRequest
}