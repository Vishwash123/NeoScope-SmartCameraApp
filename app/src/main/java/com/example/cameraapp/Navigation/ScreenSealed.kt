package com.example.cameraapp.Navigation

import android.net.Uri

const val AUTH_ROUTE = "auth"
const val MAIN_ROUTE = "home"
const val ROOT_ROUTE = "root"
sealed class ScreenSealed(val route:String) {
    object SignUp:ScreenSealed(route="signup_screen")
    object Login:ScreenSealed(route="login_screen")
    object Home:ScreenSealed(route="home_screen")
    object MainScreen:ScreenSealed(route="main_screen")
    object ChatBotScreen:ScreenSealed(route="chatbot")
    object PreviewScreen:ScreenSealed(route="preview")
    object ResponseScreen:ScreenSealed(route="response_screen?url={url}"){
        fun passAll(url:String?):String{
            return "response_screen?" +
                    "url=${Uri.encode(url)}"
        }
    }
}