package com.example.cameraapp.Components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.ChatbotViewModel

@Composable
fun ImagePreview(
    modifier:Modifier=Modifier,
    onRemoveClicked:()->Unit,
    image: Uri? =null
){
    Box(
        modifier = modifier.size(50.dp)
    ){
        Image(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
            , contentScale = ContentScale.Crop

            , painter = rememberAsyncImagePainter(image)
            , contentDescription = null
        )
        Image(
            modifier=Modifier
                .size(20.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    onRemoveClicked()
                }
            , painter = painterResource(R.drawable.cross_circle)
            , contentDescription = null
        )
    }
}

