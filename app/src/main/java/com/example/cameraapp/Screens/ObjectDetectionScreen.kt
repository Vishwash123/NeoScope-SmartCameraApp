package com.example.cameraapp.Screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cameraapp.Components.TTSManager
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ui.theme.Montserrat
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.objects.DetectedObject
import org.tensorflow.lite.task.vision.detector.Detection
import java.io.File

@Composable
fun ObjectDetectionScreen(
    context: Context,
    cameraViewModel: CameraViewModel,
    navHostController: NavHostController
){
    BackHandler(enabled = true) {
        cameraViewModel.shouldCleanUp.value = true
        navHostController.popBackStack()

    }
    val imageUri = cameraViewModel.capturedImageUri.value
    val imageBitmap = cameraViewModel.capturedImageBitmap
    var detectedObjects = remember { mutableStateListOf<Detection>() }
    LaunchedEffect(imageBitmap) {
        if(imageBitmap!=null){
            val results = cameraViewModel.detect(
                context,
                imageBitmap
            )
            detectedObjects.addAll(results)
        }
    }
    val file = imageUri?.path?.let{File(it)}
    Box(modifier = Modifier.fillMaxSize()){
        if(file!=null && file.exists()) {
            Image(
                painter = rememberAsyncImagePainter(model = file),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )
            ScanAnimation(modifier = Modifier.fillMaxSize())
            if(detectedObjects.isNotEmpty()){
                BoxWithConstraints {
                    val imageWidthPx = imageBitmap!!.width
                    val imageHeightPx = imageBitmap!!.height
                    val displayWidthPx = constraints.maxWidth
                    val displayHeightPx = constraints.maxHeight

                    val scaleX = displayWidthPx.toFloat()/imageWidthPx
                    val scaleY = displayHeightPx.toFloat()/imageHeightPx

                    detectedObjects.forEach{detectedObject->
                        detectedObject.boundingBox?.let{box->
                            val left = (box.left * scaleX).toInt()
                            val top = (box.top * scaleY).toInt()
                            val width = (box.width() * scaleX).toInt()
                            val height = (box.height() * scaleY).toInt()

                            QRResultFloatingButton(detectedObject.categories.firstOrNull()?.label?:"Object",top,left,width,height) {
                                
                            }
                        }
                    }

                }
            }
        }
    }
}





//@Preview
//@Composable
//fun Qrpreview(){
//    QRResultFloatingButton("hello") { }
//}