package com.example.cameraapp.Screens

import CustomizableGradientText
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cameraapp.Components.AnimatedImageButton
import com.example.cameraapp.Components.CameraPreviewView
//import com.example.cameraapp.Components.CarouselModeSelector
import com.example.cameraapp.Components.PerfectlyCenteredCarousel
import com.example.cameraapp.FirebaseService
import com.example.cameraapp.Models.ImageAnalysisOption
import com.example.cameraapp.Navigation.AUTH_ROUTE
import com.example.cameraapp.Navigation.ScreenSealed
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ui.theme.Montserrat

@Composable
fun MainScreen(navHostController: NavHostController,cameraViewModel: CameraViewModel= hiltViewModel()){

    val shouldCleanUp = cameraViewModel.shouldCleanUp.value

    LaunchedEffect(shouldCleanUp) {
        if (shouldCleanUp) {
            cameraViewModel.discardImage(cameraViewModel.capturedImageUri.value!!)
            cameraViewModel.shouldCleanUp.value = false
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val activity = context as Activity
    var shouldNavigateBack by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri->
        Toast.makeText(context,"Uri:$uri",Toast.LENGTH_SHORT).show()
        cameraViewModel.setUploadedImageUri(context,uri!!)
        navHostController.navigate(ScreenSealed.PreviewScreen.route)
    }

    var isFrontCamera by remember { mutableStateOf(false) }

    val cameraSelector = if(isFrontCamera) {
        CameraSelector.DEFAULT_FRONT_CAMERA
    } else CameraSelector.DEFAULT_BACK_CAMERA

    LaunchedEffect(shouldNavigateBack) {
        if (shouldNavigateBack){
            navHostController.navigate(AUTH_ROUTE){
//                popUpTo(AUTH_ROUTE){
//                    inclusive = true
//                }
                activity.finish()
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()){
        CameraPreviewView(
            lifecycleOwner = lifecycleOwner,
            onImageAnalysis = {imageProxy ->
                imageProxy.close()
            },
            onImageCaptureReady = {imageCapture->
                cameraViewModel.setImageCapture(context,imageCapture)
            },
            cameraSelector
        )
        Row(modifier = Modifier.fillMaxWidth().padding(top=60.dp, start = 20.dp, end = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Image(modifier = Modifier.height(35.dp).width(35.dp).clickable {
                FirebaseService.firebaseAuth.signOut()
                navHostController.navigate(AUTH_ROUTE)
            }, painter = painterResource(R.drawable.power_off), contentDescription = null)
            CustomizableGradientText(text = "NEO SCOPE", fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF),)
//            Image(modifier = Modifier.height(40.dp).width(40.dp), painter = painterResource(R.drawable.walk_guide), contentDescription = null)
            Image(modifier = Modifier.height(30.dp).width(30.dp).clickable {
                navHostController.navigate(ScreenSealed.ChatBotScreen.route)
            }, painter = painterResource(R.drawable.bot), contentDescription = null)
        }


        Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            PerfectlyCenteredCarousel(items = listOf("TRANSLATE","NORMAL","SELECT TEXT","OBJECT DETECTION","SCAN QR")){selectedText->
                when(selectedText){
                    "TRANSLATE"-> cameraViewModel.pickSelectedMode(ImageAnalysisOption.TRANSLATE)
                    "SCAN QR"-> cameraViewModel.pickSelectedMode(ImageAnalysisOption.QR_CODE)
                    "SELECT TEXT"-> cameraViewModel.pickSelectedMode(ImageAnalysisOption.TEXT_RECOGNITION)
                    "OBJECT DETECTION"-> cameraViewModel.pickSelectedMode(ImageAnalysisOption.OBJECT_DETECTION)
                    else-> cameraViewModel.resetSelectedMode()
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, start = 40.dp, end = 40.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.padding(bottom = 30.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(modifier = Modifier.height(30.dp).width(30.dp).clickable {
                        launcher.launch("image/*")
                    }, painter = painterResource(R.drawable.upload), contentDescription = null)
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomizableGradientText(text = "UPLOAD", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF))

                }
                 AnimatedImageButton(context, cameraViewModel = cameraViewModel,modifier = Modifier.height(128.dp).width(128.dp)){
                    navHostController.navigate(ScreenSealed.PreviewScreen.route)
                 }
                Column(modifier = Modifier.padding(bottom = 30.dp),verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(modifier = Modifier.height(30.dp).width(30.dp).clickable {
//                        navHostController.navigate(ScreenSealed.ChatBotScreen.route)
//                    }, painter = painterResource(R.drawable.bot), contentDescription = null)
                    Image(painter = painterResource(R.drawable.camera_rotate), modifier = Modifier.height(40.dp).width(40.dp).padding(top=1.dp)
                        .clickable { isFrontCamera=!isFrontCamera }, contentDescription = null)

                    Spacer(modifier = Modifier.height(6.dp))
                    CustomizableGradientText(text = "ROTATE", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF))

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

