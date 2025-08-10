package com.example.cameraapp.Navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cameraapp.Components.ResponseImagePreview
import com.example.cameraapp.Screens.ChatbotScreen
import com.example.cameraapp.Screens.MainScreen
import com.example.cameraapp.Screens.PreviewScreen
import com.example.cameraapp.Screens.TestHome
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ViewModels.ChatbotViewModel

fun NavGraphBuilder.MainNavGraph(navHostController: NavHostController) {
    navigation(startDestination = ScreenSealed.MainScreen.route, route = MAIN_ROUTE){
        composable(
            route = ScreenSealed.MainScreen.route
        ) {navBackStackEntry->
            val parentEntry = remember(navBackStackEntry) {
                navHostController.getBackStackEntry(MAIN_ROUTE)
            }
            val cameraViewModel:CameraViewModel = hiltViewModel(parentEntry)
            MainScreen(navHostController, cameraViewModel = cameraViewModel)
        }

        composable(
            route = ScreenSealed.ChatBotScreen.route
        ) {navBackStackEntry->
            val parentEntry = remember(navBackStackEntry){
                navHostController.getBackStackEntry(MAIN_ROUTE)
            }
//            val cameraViewModel:CameraViewModel = hiltViewModel(parentEntry)
            val chatbotViewModel:ChatbotViewModel = hiltViewModel(parentEntry)
            ChatbotScreen(navHostController = navHostController, chatbotViewModel = chatbotViewModel)
        }

        composable(
            route = ScreenSealed.PreviewScreen.route
        ) {navBackStackEntry->
            val parentEntry = remember(navBackStackEntry) {
                navHostController.getBackStackEntry(MAIN_ROUTE)
            }
            val cameraViewModel:CameraViewModel = hiltViewModel(parentEntry)
            val chatbotViewModel:ChatbotViewModel = hiltViewModel(parentEntry)
            PreviewScreen(navHostController = navHostController, cameraViewModel = cameraViewModel, chatbotViewModel = chatbotViewModel)
        }

        composable(
            route = "response_screen?url={url}"
        ) {navBackStackEntry->
            val parentEntry = remember(navBackStackEntry) {
                navHostController.getBackStackEntry(MAIN_ROUTE)
            }
            val url = navBackStackEntry.arguments?.getString("url")
            val chatbotViewModel:ChatbotViewModel = hiltViewModel(parentEntry)
            ResponseImagePreview(navHostController = navHostController, chatbotViewModel = chatbotViewModel, url = url)
        }
    }
}