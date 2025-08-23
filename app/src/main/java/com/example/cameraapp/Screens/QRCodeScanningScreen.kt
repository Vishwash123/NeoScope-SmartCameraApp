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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ui.theme.Montserrat
import com.google.mlkit.vision.barcode.common.Barcode
import java.io.File

@Composable
fun QRCodeScanningScreen(
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
    var barcodes = remember { mutableStateListOf<Barcode>() }
    LaunchedEffect(imageBitmap) {
        if(imageBitmap!=null){
            cameraViewModel.processQRCode(
                bitmap = imageBitmap,
                onResult = {results->
                    barcodes.addAll(results)
                },
                onError ={

                }
            )
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
            if(barcodes.isNotEmpty()){
                BoxWithConstraints {
                    val imageWidthPx = imageBitmap!!.width
                    val imageHeightPx = imageBitmap!!.height
                    val displayWidthPx = constraints.maxWidth
                    val displayHeightPx = constraints.maxHeight
                    
                    val scaleX = displayWidthPx.toFloat()/imageWidthPx
                    val scaleY = displayHeightPx.toFloat()/imageHeightPx

                    barcodes.forEach{barcode->
                        barcode.boundingBox?.let{box->
                            val left = (box.left * scaleX).toInt()
                            val top = (box.top * scaleY).toInt()
                            val width = (box.width() * scaleX).toInt()
                            val height = (box.height() * scaleY).toInt()

                            QRResultFloatingButton(barcode.rawValue?:"",top,left,width,height) {
                                cameraViewModel.openQRContent(context,barcode.rawValue?:"")
                            }
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun ScanAnimation(
    modifier: Modifier=Modifier,
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.scan_matrix))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        modifier = Modifier.fillMaxSize().graphicsLayer { scaleY=3f },
        composition=composition,
        progress = {progress}
    )
}

@Composable
fun QRResultFloatingButton(
    qrText: String,
    top:Int,
    left:Int,
    width:Int,
    height:Int,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .offset { IntOffset(left, top) }
            .width(200.dp)
            .height(60.dp)
            .padding(10.dp)
            .wrapContentSize()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Blue
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = qrText,
                fontFamily = Montserrat,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

