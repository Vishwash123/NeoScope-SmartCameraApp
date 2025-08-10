package com.example.cameraapp.Components

import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

//@Composable
//fun CameraPreviewView(
//    lifecycleOwner: LifecycleOwner,
//    onImageAnalysis:(ImageProxy)->Unit,
//    onImageCaptureReady:(ImageCapture)->Unit,
//    cameraSelector: CameraSelector
//){
//    val context = LocalContext.current
//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//    val previewView  = PreviewView(ctx).apply {
//        layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//    }
//
//    val cameraProvider = cameraProviderFuture.get()
//    val preview = Preview.Builder().build().also{
//        it.setSurfaceProvider(previewView.surfaceProvider)
//    }
//
////        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//    val imageCapture = ImageCapture.Builder().setTargetRotation(Surface.ROTATION_0).build()
//
//    onImageCaptureReady(imageCapture)
//
//    val imageAnalysis = ImageAnalysis.Builder()
//        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//        .build()
//        .also {
//            it.setAnalyzer(ContextCompat.getMainExecutor(ctx),onImageAnalysis)
//        }
//
//    cameraProvider.unbindAll()
//    cameraProvider.bindToLifecycle(
//        lifecycleOwner,
//        cameraSelector,
//        preview,
//        imageAnalysis,
//        imageCapture
//    )
//
//    AndroidView(factory = {ctx->
//
//        previewView
//
//    }, modifier = Modifier.fillMaxSize())
//}

@Composable
fun CameraPreviewView(
    lifecycleOwner: LifecycleOwner,
    onImageAnalysis: (ImageProxy) -> Unit,
    onImageCaptureReady: (ImageCapture) -> Unit,
    cameraSelector: CameraSelector
) {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    // Rebind camera every time cameraSelector changes
    LaunchedEffect(cameraSelector) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageCapture = ImageCapture.Builder()
            .setTargetRotation(Surface.ROTATION_0)
            .build()
        onImageCaptureReady(imageCapture)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(context), onImageAnalysis)
            }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis,
            imageCapture
        )
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}
