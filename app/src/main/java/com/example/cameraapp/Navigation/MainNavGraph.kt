package com.example.cameraapp.Navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cameraapp.Screens.ChatbotScreen
import com.example.cameraapp.Screens.MainScreen
import com.example.cameraapp.Screens.TestHome

fun NavGraphBuilder.MainNavGraph(navHostController: NavHostController) {
    navigation(startDestination = ScreenSealed.MainScreen.route, route = MAIN_ROUTE){
        composable(
            route = ScreenSealed.MainScreen.route
        ) {
            MainScreen(navHostController)
        }

        composable(
            route = ScreenSealed.ChatBotScreen.route
        ) {
            ChatbotScreen(navHostController = navHostController)
        }
    }
}