package com.example.cameraapp.Navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.cameraapp.FirebaseService
import com.example.cameraapp.GoogleAuthHandler



val LocalGoogleAuth = staticCompositionLocalOf<GoogleAuthHandler> {
    error("No GoogleAuthHandler provided")
}


@Composable
fun RootNavGraph (
    navHostController: NavHostController
){
    val isLoggedIn = remember { FirebaseService.firebaseAuth.currentUser!=null }
    var googleAuthHandler by remember { mutableStateOf<GoogleAuthHandler?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        googleAuthHandler?.handleResult(result)
    }
    val context = LocalContext.current

    googleAuthHandler = GoogleAuthHandler(
        context = context,
        auth = FirebaseService.firebaseAuth,
        oneTapClient = FirebaseService.oneTapClient,
        signInRequest = FirebaseService.signInRequest,
        launcher = launcher,
        onSuccess = { navHostController.navigate("home"){
            popUpTo(AUTH_ROUTE){
                inclusive = true
            }


        } }
    )

    CompositionLocalProvider(LocalGoogleAuth provides googleAuthHandler!!) {
        NavHost(
            navController = navHostController,
            startDestination = if (!isLoggedIn) {
                AUTH_ROUTE
            } else {
                MAIN_ROUTE
            },
            route = ROOT_ROUTE,
            enterTransition = { defaultEnter() },
            exitTransition = { defaultExit() },
            popEnterTransition = { defaultPopEnter() },
            popExitTransition = { defaultPopExit() }
        ) {

            AuthNavGraph(navHostController)
            MainNavGraph(navHostController)
        }
    }
}