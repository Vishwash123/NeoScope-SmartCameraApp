package com.example.cameraapp.Components

import android.content.Context

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cameraapp.ViewModels.CameraViewModel


@Composable
fun AnimatedImageButton(context: Context,modifier: Modifier,cameraViewModel: CameraViewModel= hiltViewModel(),onImageCaptured:()->Unit){

    val scope = rememberCoroutineScope()

    IconButton(
            modifier = modifier,
            onClick = {
                cameraViewModel.animateShutter(scope)
                cameraViewModel.captureImage(context){uri->
                    onImageCaptured()
                }
            }
        ) {
            Image(modifier = Modifier.width(128.dp).height(128.dp), painter = painterResource(id = cameraViewModel.frames[cameraViewModel.currentFrame]), contentDescription = null)
        }


}
