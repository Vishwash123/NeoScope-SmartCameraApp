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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
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

    var contentScale by remember { mutableStateOf<ContentScale>(ContentScale.Crop) }




    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),

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

        Column(
            modifier=Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(url)
                    .listener(
                        onSuccess = { _, result ->
                            val size = (result as? SuccessResult)?.drawable?.intrinsicWidth to
                                    (result as? SuccessResult)?.drawable?.intrinsicHeight

                            if (size != null) {
                                contentScale = if (size.first!! > size.second!!) {
                                    ContentScale.FillBounds
                                } else {
                                    ContentScale.Crop
                                }
                            }
                        }
                    )
                    .build(),
                contentDescription = null,
                modifier = modifier,
                contentScale = contentScale
            )
        }



    }
}
