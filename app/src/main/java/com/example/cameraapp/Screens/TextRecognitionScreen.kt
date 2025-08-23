package com.example.cameraapp.Screens

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cameraapp.Components.TTSManager
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ui.theme.Montserrat
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextRecognitionScreen(
    context: Context,
    modifier: Modifier = Modifier,
    cameraViewModel: CameraViewModel= hiltViewModel(),
    navHostController: NavHostController
) {

    BackHandler(enabled = true) {
        cameraViewModel.shouldCleanUp.value = true
        TTSManager.stop()
        navHostController.popBackStack()

    }
    val clipboardManager = LocalClipboardManager.current
    var text by remember { mutableStateOf("") }
    var failed by remember { mutableStateOf(false) }
    val imageUri = cameraViewModel.capturedImageUri.value
    val imageBitmap = cameraViewModel.capturedImageBitmap
    LaunchedEffect(imageBitmap) {
        if(imageBitmap!=null){
            cameraViewModel.processTextRecognition(
                imageBitmap,
                onResult = {result->
                    text = result
                    failed = false
                },
                onError = {
                    text = "Text recognition failed"
                    failed = true
                }
            )
        }
    }

    var isSheetOpen by remember { mutableStateOf(true) }
    val file = imageUri?.path?.let { File(it) }
    Box(modifier = modifier.fillMaxSize().background(Color.Black)){
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
                contentDescription = null,
                modifier = modifier.fillMaxSize(),
                contentScale = contentScale
            )
        }
        else{
        }

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

        if(isSheetOpen) {
            ModalBottomSheet(
                modifier = Modifier.wrapContentSize(),
                onDismissRequest = { isSheetOpen = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                contentColor = Color.Transparent,
                dragHandle = null
            ) {
                Box(
                    modifier=Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .border(width = 2.dp,shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp), color = Color.White)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.chatbot_bg),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null
                    )
                    BottomSheetContent(text,failed,context,clipboardManager)
                }
            }
        }
        else{
            AnimatedDisplayButton(modifier=modifier.align(Alignment.BottomCenter), animationRes = R.raw.arrow) {
                isSheetOpen = true
            }
        }
    }
}


@Composable
fun BottomSheetContent(
    text: String,
    failureState:Boolean,
    context: Context,
    clipboardManager: androidx.compose.ui.platform.ClipboardManager
){
    Column(
        modifier = Modifier.height(500.dp).fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scrollState = rememberScrollState()


        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Extracted Text",
            fontSize = 36.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        SelectionContainer(modifier = Modifier.wrapContentSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 250.dp)
                    .verticalScroll(scrollState)
                    .padding(8.dp)
            ){
                Text(
                    text = text,
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = if (!failureState) Color.White else Color.Red,
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.wrapContentSize(), horizontalArrangement = Arrangement.spacedBy(40.dp)) {
            TextRecognitionButton(image = R.drawable.copy,text = "Copy"){
                clipboardManager.setText(AnnotatedString(text))
                Toast.makeText(context,"Text copied",Toast.LENGTH_SHORT).show()
            }
            TextRecognitionButton(image = R.drawable.speak, text = "Read out") {
                TTSManager.speak(text)
            }
            TextRecognitionButton(image = R.drawable.share, text = "Share") {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT,text)
                }
                context.startActivity(Intent.createChooser(intent,"Share text via"))
            }

        }
    }
}

@Composable
fun TextRecognitionButton(
    image:Int,
    text:String,
    onClick:()->Unit
){
    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0f to Color(0xFF48687D),
            1f to Color(0xFF257BB7)
        )
    )
    Row(
        modifier = Modifier
            .height(42.dp)
            .width(100.dp)
            .background(shape = RoundedCornerShape(4.dp), brush = gradient)
            .border(1.dp,Color.White, shape = RoundedCornerShape(4.dp))
            .padding(8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(image),
            contentDescription = null
        )
        Text(
            text=text,
            color = Color.White,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}

@Composable
fun AnimatedDisplayButton(
    modifier: Modifier = Modifier,
    animationRes:Int,
    onClick: () -> Unit
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationRes))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever
    )
    Box(modifier=modifier.wrapContentSize().clickable { onClick() }){
        LottieAnimation(
            modifier = Modifier.size(200.dp).graphicsLayer { rotationZ = 180f },
            composition = composition,
            progress = {progress}
        )
    }
}
