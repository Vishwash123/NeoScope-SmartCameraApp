package com.example.cameraapp.Screens

import CustomizableGradientText
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cameraapp.Components.AnimatedImageButton
import com.example.cameraapp.Components.CameraPreviewView
//import com.example.cameraapp.Components.CarouselModeSelector
import com.example.cameraapp.Components.PerfectlyCenteredCarousel
import com.example.cameraapp.FirebaseService
import com.example.cameraapp.Navigation.AUTH_ROUTE
import com.example.cameraapp.Navigation.ScreenSealed
import com.example.cameraapp.R
import com.example.cameraapp.ui.theme.Montserrat

@Composable
fun MainScreen(navHostController: NavHostController){
    val lifecycleOwner = LocalLifecycleOwner.current
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


    Box(modifier = Modifier.fillMaxSize()){
        CameraPreviewView(
            lifecycleOwner = lifecycleOwner,
            onImageAnalysis = {imageProxy ->
                imageProxy.close()
            }
        )
        Row(modifier = Modifier.fillMaxWidth().padding(top=60.dp, start = 20.dp, end = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Image(modifier = Modifier.height(40.dp).width(40.dp), painter = painterResource(R.drawable.close), contentDescription = null)
            CustomizableGradientText(text = "NEO SCOPE", fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF),)
            Image(modifier = Modifier.height(40.dp).width(40.dp), painter = painterResource(R.drawable.walk_guide), contentDescription = null)
        }

        Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 40.dp)) {
            PerfectlyCenteredCarousel(items = listOf("TRANSLATE","NORMAL","SELECT TEXT","OBJECT DETECTION"))
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, start = 40.dp, end = 40.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.padding(bottom = 14.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(modifier = Modifier.height(30.dp).width(30.dp), painter = painterResource(R.drawable.upload), contentDescription = null)
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomizableGradientText(text = "UPLOAD", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF))

                }
                 AnimatedImageButton(modifier = Modifier.height(128.dp).width(128.dp))
                Column(modifier = Modifier.padding(bottom = 14.dp),verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(modifier = Modifier.height(30.dp).width(30.dp).clickable {
                        navHostController.navigate(ScreenSealed.ChatBotScreen.route)
                    }, painter = painterResource(R.drawable.bot), contentDescription = null)
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomizableGradientText(text = "AI CHAT", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF))

                }
            }
        }





    }
}

@Preview
@Composable
fun MainScreenPreview(){
    MainScreen(rememberNavController())
}

