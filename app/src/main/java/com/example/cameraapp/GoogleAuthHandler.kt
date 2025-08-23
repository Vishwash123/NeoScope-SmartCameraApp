package com.example.cameraapp

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthHandler(
    private val context: Context,
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    private val signInRequest: BeginSignInRequest,
    private val launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    private val onSuccess: () -> Unit
) {

    fun launchGoogleSignIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                val intentSenderRequest =
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                launcher.launch(intentSenderRequest)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "One Tap failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context,"Sign Up Successful",Toast.LENGTH_SHORT).show()
                                onSuccess()
                            } else {
                                Toast.makeText(context, "Firebase sign in failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else{
                    Toast.makeText(context, "Id Token null", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
