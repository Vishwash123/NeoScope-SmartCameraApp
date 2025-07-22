package com.example.cameraapp.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimatedImageButton(modifier: Modifier,cameraViewModel: CameraViewModel= hiltViewModel()){



    var isAnimating by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    IconButton(
        modifier = modifier,
        onClick = {
//
//            if(!isAnimating){
//                isAnimating = true
//                scope.launch {
//                    for(i in frames.indices){
//                        currentFrame = i;
//                        delay(50L)
//                    }
//                }
//                isAnimating = false
//            }
            cameraViewModel.animateShutter(scope)
        }
    ) {
        Image(modifier = Modifier.width(128.dp).height(128.dp), painter = painterResource(id = cameraViewModel.frames[cameraViewModel.currentFrame]), contentDescription = null)
    }
}
