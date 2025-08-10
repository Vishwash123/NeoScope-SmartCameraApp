package com.example.cameraapp.Screens

import CustomizableGradientText
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.cameraapp.Components.AnimatedImageButton
import com.example.cameraapp.Components.CameraPreviewView
import com.example.cameraapp.Components.PerfectlyCenteredCarousel
import com.example.cameraapp.Models.ImageAnalysisOption
import com.example.cameraapp.Navigation.AUTH_ROUTE
import com.example.cameraapp.Navigation.ScreenSealed
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ViewModels.ChatbotViewModel
import com.example.cameraapp.ui.theme.Montserrat
import java.io.File


@Composable
fun PreviewScreen(
    modifier: Modifier = Modifier,
    cameraViewModel: CameraViewModel= hiltViewModel(),
    chatbotViewModel: ChatbotViewModel = hiltViewModel(),
    navHostController: NavHostController
){
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val currentMode = cameraViewModel.selectedMode

    when(currentMode){
        ImageAnalysisOption.TEXT_RECOGNITION -> TextRecognitionScreen(context=context,modifier=modifier,cameraViewModel,navHostController)
        ImageAnalysisOption.QR_CODE -> QRCodeScanningScreen(context = context,cameraViewModel=cameraViewModel,navHostController=navHostController)
        ImageAnalysisOption.OBJECT_DETECTION -> ObjectDetectionScreen(context,cameraViewModel,navHostController)
        ImageAnalysisOption.TRANSLATE -> TranslationScreen(context = context, cameraViewModel = cameraViewModel, navHostController = navHostController, modifier = modifier)
        else-> ImagePreviewScreen(cameraViewModel=cameraViewModel, chatbotViewModel = chatbotViewModel, navHostController = navHostController, context = context)

    }
}