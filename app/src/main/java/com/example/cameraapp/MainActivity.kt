package com.example.cameraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cameraapp.Navigation.RootNavGraph
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ui.theme.CameraAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CameraAppTheme {
                val cameraViewModel: CameraViewModel = hiltViewModel()
                navHostController = rememberNavController()
                RootNavGraph(navHostController)
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {


}

