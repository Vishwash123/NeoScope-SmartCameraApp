package com.example.cameraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cameraapp.Navigation.RootNavGraph
import com.example.cameraapp.Screens.QRResultFloatingButton
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
                val cameraViewModel: CameraViewModel = hiltViewModel() // Scoped to activity
//
                navHostController = rememberNavController()
                RootNavGraph(navHostController)

//                TextRecognitionScreen(cameraViewModel = hiltViewModel())
//                Greeting("hello")

            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Column( modifier = Modifier.fillMaxSize().background(Color.Black), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
//        QRResultFloatingButton("hELLLLLO") { }
//    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CameraAppTheme {
        Greeting("Android")
    }
}