package com.example.cameraapp.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cameraapp.FirebaseService
import com.example.cameraapp.Navigation.AUTH_ROUTE


@SuppressLint("RememberReturnType")
@Composable
fun TestHome(navHostController: NavHostController){

    var shouldNavigateBack by remember { mutableStateOf(false) }

    LaunchedEffect(shouldNavigateBack) {
        if (shouldNavigateBack){
            navHostController.navigate(AUTH_ROUTE){
                popUpTo(AUTH_ROUTE){
                    inclusive = true
                }
            }
        }
    }

    val currentUserName = remember  { mutableStateOf( FirebaseService.firebaseAuth.currentUser?.displayName?:"")}
    val currentUserId = remember  { mutableStateOf( FirebaseService.firebaseAuth.currentUser?.uid?:"")}
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Username : ${currentUserName.value}", color = Color.Black, fontSize = 24.sp)
        Spacer(Modifier.height(12.dp))
        Text(text = "Uid : ${currentUserId.value}", color = Color.Black, fontSize = 24.sp)
        Spacer(Modifier.height(12.dp))
        Text(text = "   LOG OUT", color = Color.Red, fontSize = 44.sp, modifier = Modifier.clickable {
            shouldNavigateBack = true
            FirebaseService.firebaseAuth.signOut()

        })

    }


}

@Preview(showBackground = true)
@Composable
fun TestHomePreview(){
    TestHome(rememberNavController())
}