package com.example.cameraapp.ViewModels

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cameraapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor():ViewModel(){

    private var shutterAnimating by mutableStateOf(false)
    var currentFrame by mutableStateOf(0)
    val frames = listOf(
        R.drawable.cam_open,
        R.drawable.camera_mid,
        R.drawable.camera_close,
        R.drawable.camera_mid,
        R.drawable.cam_open
    )

    private var capturedBitmap by mutableStateOf<Bitmap?>(null)

    fun getCapturedBitmap():Bitmap?{
        return capturedBitmap
    }

    fun setCapturedBitmap(bitmap: Bitmap){
        capturedBitmap = bitmap
    }

    fun animateShutter(scope:CoroutineScope){
        if(!shutterAnimating){
            shutterAnimating = true
            scope.launch {
                for (i in frames.indices){
                    currentFrame = i
                    delay(20L)
                }
            }
            shutterAnimating = false
        }
    }

}