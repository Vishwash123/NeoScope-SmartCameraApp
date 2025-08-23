package com.example.cameraapp.Screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
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
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)){
        if(file!=null && file.exists()) {
            val options = remember(file) {
                BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    .also { BitmapFactory.decodeFile(file.absolutePath, it) }
            }

            val contentScale = remember(options.outWidth to options.outHeight) {
                if (options.outWidth > options.outHeight) {
                    ContentScale.Fit
                } else {
                    ContentScale.Crop
                }
            }
            Image(
                painter = BitmapPainter(BitmapFactory.decodeFile(file.path).asImageBitmap()),
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )

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
            }

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
