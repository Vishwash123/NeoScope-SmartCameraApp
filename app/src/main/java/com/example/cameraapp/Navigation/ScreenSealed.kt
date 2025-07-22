package com.example.cameraapp.Navigation

const val AUTH_ROUTE = "auth"
const val MAIN_ROUTE = "home"
const val ROOT_ROUTE = "root"
sealed class ScreenSealed(val route:String) {
    object SignUp:ScreenSealed(route="signup_screen")
    object Login:ScreenSealed(route="login_screen")
    object Home:ScreenSealed(route="home_screen")
    object MainScreen:ScreenSealed(route="main_screen")
    object ChatBotScreen:ScreenSealed(route="chatbot")
}