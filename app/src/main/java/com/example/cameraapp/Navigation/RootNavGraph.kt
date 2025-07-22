package com.example.cameraapp.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.cameraapp.FirebaseService
import com.google.firebase.auth.FirebaseAuth


@Composable
fun RootNavGraph (
    navHostController: NavHostController
){
    val isLoggedIn = remember { FirebaseService.firebaseAuth.currentUser!=null }
    NavHost(navController = navHostController, startDestination = if(!isLoggedIn){
        AUTH_ROUTE
    }
    else{
        MAIN_ROUTE
    }
    , route = ROOT_ROUTE
    ) {

        AuthNavGraph(navHostController)
        MainNavGraph(navHostController)
    }
}