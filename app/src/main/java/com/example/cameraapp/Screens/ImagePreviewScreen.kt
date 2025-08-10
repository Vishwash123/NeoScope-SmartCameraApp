package com.example.cameraapp.Screens

import CustomizableGradientText
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cameraapp.Components.TTSManager
import com.example.cameraapp.Navigation.ScreenSealed
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ViewModels.ChatbotViewModel
import com.example.cameraapp.ui.theme.Montserrat
import java.io.File

@Composable
fun ImagePreviewScreen(
    cameraViewModel: CameraViewModel,
    chatbotViewModel:ChatbotViewModel,
    navHostController: NavHostController,
    context: Context
){

    BackHandler(enabled = true) {
        cameraViewModel.shouldCleanUp.value = true
        navHostController.popBackStack()
    }

    val imageUri = cameraViewModel.capturedImageUri.value
//    if(imageUri==null){
//    Toast.makeText(context,"$currentMode", Toast.LENGTH_LONG).show()
//    }

    imageUri?.let { uri ->


        Box(modifier = Modifier.fillMaxSize()) {
//        CameraPreviewView(
//            lifecycleOwner = lifecycleOwner,
//            onImageAnalysis = {imageProxy ->
//                imageProxy.close()
//            },
//            onImageCaptureReady = {imageCapture->
//                cameraViewModel.setImageCapture(imageCapture)
//            }
//        )
            val file = imageUri?.path?.let { File(it) }

            if (file != null && file.exists()) {
                Image(
                    painter = rememberAsyncImagePainter(model = file),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Toast.makeText(context, "Image file not found!", Toast.LENGTH_SHORT).show()
            }

//            Image(
//                painter = rememberAsyncImagePainter(cameraViewModel.capturedImageUri),
//                modifier = Modifier.fillMaxSize(),
//                contentDescription = null,
//                contentScale = ContentScale.Crop
//            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 60.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier.height(40.dp).width(40.dp)
                        .clickable {
                            cameraViewModel.discardImage(imageUri!!)
                            navHostController.popBackStack()
                        },
                    painter = painterResource(R.drawable.back),
                    contentDescription = null
                )
//            CustomizableGradientText(text = "NEO SCOPE", fontSize = 32.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF),)
//                Image(
//                    modifier = Modifier.height(40.dp).width(40.dp),
//                    painter = painterResource(R.drawable.walk_guide),
//                    contentDescription = null
//                )
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .padding(bottom = 40.dp)
            ) {
//                PerfectlyCenteredCarousel(
//                    items = listOf(
//                        "TRANSLATE",
//                        "NORMAL",
//                        "SELECT TEXT",
//                        "OBJECT DETECTION"
//                    )
//                )
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, start = 40.dp, end = 40.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.padding(bottom = 30.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(modifier = Modifier.height(30.dp).width(30.dp)
                            .clickable {
                                cameraViewModel.saveToGallery(context,imageUri!!)
                            }
                            , painter = painterResource(R.drawable.save_image), contentDescription = null)
                        Spacer(modifier = Modifier.height(10.dp).padding(start =65.dp))
                        CustomizableGradientText(text = "SAVE", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF))

                    }
//                    AnimatedImageButton(context, cameraViewModel = cameraViewModel,modifier = Modifier.height(128.dp).width(128.dp)){
//                        navHostController.navigate(ScreenSealed.PreviewScreen.route)
//                    }
//                    Spacer(modifier = Modifier.width(2.dp))
                    IconButton(
                        modifier = Modifier.height(118.dp).width(118.dp).padding(bottom = 10.dp),
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Image(modifier = Modifier.fillMaxSize(),
                            painter = painterResource(R.drawable.discard2), contentDescription = null
                        )
                    }

                    Column(modifier = Modifier.padding(bottom = 30.dp),verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(modifier = Modifier.height(30.dp).width(30.dp).clickable {
                            chatbotViewModel.setImageUri(imageUri)
                            navHostController.navigate(ScreenSealed.ChatBotScreen.route){
                                popUpTo(ScreenSealed.PreviewScreen.route) {
                                    inclusive = true // removes PreviewScreen from backstack
                                }
                            }

                        }, painter = painterResource(R.drawable.bot), contentDescription = null)
                        Spacer(modifier = Modifier.height(10.dp))
                        CustomizableGradientText(text = "AI CHAT", fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, gradientStartColor = Color(0xFFDBEBF2), gradientEndColor = Color(0xFFFFFFFF), dropShadowColor = Color(0xFFFFFFFF))

                    }
                }

            }


        }
    }
}