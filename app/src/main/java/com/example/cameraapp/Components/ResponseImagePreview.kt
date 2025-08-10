package com.example.cameraapp.Components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.ChatbotViewModel

@Composable
fun ResponseImagePreview(
    modifier: Modifier = Modifier,
    chatbotViewModel: ChatbotViewModel = hiltViewModel(),
    navHostController: NavHostController,
    url:String?=null
){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp,top=64.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navHostController.popBackStack()
                    }
                ,
                painter = painterResource(R.drawable.back),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        if(url==null){
                            Toast.makeText(context,"File unavailable",Toast.LENGTH_SHORT).show()
                            return@clickable
                        }
                        else {
                            chatbotViewModel.dowloadImage(context, url!!)
                        }
                    }
                ,
                painter = painterResource(R.drawable.export),
                contentDescription = null
            )


        }

        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = rememberAsyncImagePainter(url),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

    }
}
@Preview
@Composable
fun ResponsImagePreviewPrev(){
    ResponseImagePreview(navHostController = rememberNavController())
}