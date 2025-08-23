package com.example.cameraapp.Screens



import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.cameraapp.Models.ImageAnalysisOption
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ViewModels.ChatbotViewModel


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