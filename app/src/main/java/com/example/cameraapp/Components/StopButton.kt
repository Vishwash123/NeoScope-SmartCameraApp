package com.example.cameraapp.Components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cameraapp.R

@Composable
fun StopButton(
    modifier: Modifier = Modifier,
    onStopClicked:()->Unit,
    isVisible:Boolean
){
    val alpha by animateFloatAsState(if(isVisible) 1f else 0f)
    val offsetY by animateFloatAsState(if(isVisible) 0f else 100f)

    Image(
        modifier = modifier.size(80.dp).graphicsLayer {
            this.alpha = alpha
            this.translationY = offsetY
        }.clickable(enabled = isVisible) {
            onStopClicked()
        },
        painter = painterResource(R.drawable.stop),
        contentDescription = null
    )
}