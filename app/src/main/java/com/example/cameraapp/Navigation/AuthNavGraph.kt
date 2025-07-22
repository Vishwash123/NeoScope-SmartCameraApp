package com.example.cameraapp.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cameraapp.Screens.LogIn
import com.example.cameraapp.Screens.SignUp

fun NavGraphBuilder.AuthNavGraph(navHostController: NavHostController) {
    navigation(startDestination = ScreenSealed.SignUp.route , route = AUTH_ROUTE){
        composable(
            route = ScreenSealed.SignUp.route
        ){
            SignUp(navHostController)
        }
        composable(
            route = ScreenSealed.Login.route
        ) {
            LogIn(navHostController)
        }
    }
}