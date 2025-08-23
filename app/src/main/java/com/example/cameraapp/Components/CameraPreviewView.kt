package com.example.cameraapp.Components

import android.view.Surface
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner



@Composable
fun CameraPreviewView(
    lifecycleOwner: LifecycleOwner,
    onImageAnalysis: (ImageProxy) -> Unit,
    onImageCaptureReady: (ImageCapture) -> Unit,
    cameraSelector: CameraSelector
) {
    val context = LocalContext.current

    val previewView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    DisposableEffect(cameraSelector, lifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        val executor = ContextCompat.getMainExecutor(context)
        val listener = Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageCapture = ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .setFlashMode(ImageCapture.FLASH_MODE_OFF)
                .build()
            onImageCaptureReady(imageCapture)

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(executor, onImageAnalysis)
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis,
                    imageCapture
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }

        cameraProviderFuture.addListener(listener, executor)

        onDispose {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}
