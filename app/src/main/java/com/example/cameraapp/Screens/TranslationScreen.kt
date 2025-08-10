package com.example.cameraapp.Screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cameraapp.Components.TTSManager
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.CameraViewModel
import com.example.cameraapp.ui.theme.Montserrat
import com.google.mlkit.nl.translate.TranslateLanguage
import java.io.File
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    modifier: Modifier=Modifier,
    context: Context,
    cameraViewModel: CameraViewModel,
    navHostController: NavHostController
){
    BackHandler(enabled = true) {
        cameraViewModel.shouldCleanUp.value = true
        TTSManager.stop()
        navHostController.popBackStack()

    }
    val clipboardManager = LocalClipboardManager.current
    var sourceText by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("") }

    var languages = cameraViewModel.languages
    var languageMap = cameraViewModel.languageMap
    var revLanguageMap = cameraViewModel.codeToNameMap

    var failed by remember { mutableStateOf(false) }
    val imageUri = cameraViewModel.capturedImageUri.value
    val imageBitmap = cameraViewModel.capturedImageBitmap
    var sourceLanguage by remember { mutableStateOf<String>("Select Language") }
    var targetLanguage by remember { mutableStateOf<String>("Select Language") }

    var expandedSource by remember { mutableStateOf(false) }
    var expandedTarget by remember { mutableStateOf(false) }
    LaunchedEffect(imageBitmap,sourceLanguage,targetLanguage) {
        if(imageBitmap!=null && sourceLanguage!="Select Language" && targetLanguage!="Select Language"){

            cameraViewModel.processTranslation(
                imageBitmap!!,
                sourceLanguage,
                targetLanguage,
                onResult = {source,target->
                    sourceText = source
                    targetText = target
                    failed = false
                },
                onError = {
                    failed = true
                }
            )
        }
    }

    var isSheetOpen by remember { mutableStateOf(true) }
    val file = imageUri?.path?.let { File(it) }
    Box(modifier = modifier.fillMaxSize()){
        if(file!=null && file.exists()) {
            Image(
                painter = rememberAsyncImagePainter(model = file),
                contentDescription = null,
                modifier = modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        else{
            Toast.makeText(context,"File doesn't exist", Toast.LENGTH_SHORT).show()
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding( vertical = 100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DropDownMenuBox(
                languageList = languages,
                languageMap = languageMap,
                modifier = modifier,
                boxLabel = "From",
                expanded = expandedSource,
                selectedOption = revLanguageMap[sourceLanguage]?:"Select Language",
                onLanguageSelected = {language->
                    sourceLanguage = language
                    expandedSource = false
                },
                onExpandedChange = {exp->
                    expandedSource = exp
                }
            )

            DropDownMenuBox(
                languageList = languages,
                languageMap = languageMap,
                modifier = modifier,
                boxLabel = "To",
                expanded = expandedTarget,
                selectedOption = revLanguageMap[targetLanguage]?:"Select Language",
                onLanguageSelected = {language->
                    targetLanguage = language
                    expandedTarget = false
                },
                onExpandedChange = {exp->
                    expandedTarget = exp
                }
            )
        }

        if(isSheetOpen && sourceLanguage!="Select Language" && targetLanguage!="Select Language") {
            ModalBottomSheet(
                modifier = Modifier.wrapContentSize(),
                onDismissRequest = { isSheetOpen = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                contentColor = Color.Transparent,
                dragHandle = null
            ) {
                Box(
                    modifier= Modifier
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
                    TranslateBottomSheetContent(
                        sourceLanguage,
                        targetLanguage,
                        sourceText,
                        targetText,
                        cameraViewModel.codeToNameMap,
                        failed,
                        context,
                        clipboardManager
                    )
                }
            }
        }
        else{
            if(sourceLanguage!="Select Language" && targetLanguage!="Select Language") {
                AnimatedDisplayButton(
                    modifier = modifier.align(Alignment.BottomCenter),
                    animationRes = R.raw.arrow
                ) {
                    isSheetOpen = true
                }
            }
        }
    }
}

@Composable
fun DropDownMenuBox(
    languageList:List<String>,
    languageMap:Map<String,String>,
    modifier: Modifier=Modifier,
    boxLabel:String="",
    expanded:Boolean,
    selectedOption:String,
    onLanguageSelected:(String)->Unit,
    onExpandedChange:(Boolean)->Unit,
){
    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0f to Color(0xFF48687D),
            1f to Color(0xFF257BB7)
        )
    )
    val gradient2 = Brush.horizontalGradient(
        colorStops = arrayOf(
            0f to Color(0xFF4EA8E0),
            1f to Color(0xFF115E97)
        )
    )

    Column(
        modifier=Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = boxLabel,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            fontFamily = Montserrat,
            fontSize = 16.sp
        )

        Column(
            modifier = Modifier
                .height(42.dp)
                .width(120.dp)
                .padding(4.dp)
                .background(shape = RoundedCornerShape(4.dp), brush = gradient)
                .border(1.dp,Color.White, shape = RoundedCornerShape(4.dp))
                .clickable { onExpandedChange(if (!expanded) true else false) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = selectedOption,
                color = Color.White,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp
            )
        }

//        Box(){
//
//            DropdownMenu(
//                modifier = Modifier
//                    .background(brush = gradient, shape = RoundedCornerShape(8.dp))
//                    .border(shape = RoundedCornerShape(8.dp), color = Color.White, width = 1.dp)
//                    .heightIn(max = 400.dp)
//                    .width(100.dp),
//                expanded = expanded,
//                onDismissRequest = {onExpandedChange(false)}
//            )
        Box(
            modifier = Modifier.width(120.dp), // same width as the dropdown trigger
            contentAlignment = Alignment.TopCenter // align menu center to trigger
        ) {
//            androidx.compose.animation.AnimatedVisibility(
//                visible = expanded,
//                enter = fadeIn() + scaleIn(),
//                exit = fadeOut() + scaleOut()
//            ) {
                DropdownMenu(
                    modifier = Modifier
                        .offset { IntOffset(x = 0, y = 0) } // optional if misaligned vertically
                        .background(brush = gradient, shape = RoundedCornerShape(8.dp))
                        .border(shape = RoundedCornerShape(8.dp), color = Color.White, width = 1.dp)
                        .heightIn(max = 400.dp)
                        .width(120.dp), // slightly wider than trigger if needed
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) }
                )

                {
                    languageList.forEach { lang ->
                        DropdownMenuItem(
                            modifier = Modifier.wrapContentWidth(),
                            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
                            onClick = { onLanguageSelected(languageMap[lang] ?: "") },
                            text = {
                                Column(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .height(42.dp)
                                        .width(120.dp)
                                        .padding(4.dp)
                                        .background(
                                            shape = RoundedCornerShape(4.dp),
                                            brush = gradient2
                                        )
                                        .border(
                                            1.dp,
                                            Color.White,
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = lang,
                                        color = Color.White,
                                        fontFamily = Montserrat,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        )
                    }
                }
//            }
        }
    }

}


@Composable
fun TranslateBottomSheetContent(
    sourceLang:String,
    targetLang:String,
    sourceText: String,
    targetText:String,
    reverseLangMap:Map<String,String>,
    failureState:Boolean,
    context: Context,
    clipboardManager: androidx.compose.ui.platform.ClipboardManager
){
    val outerScrollState = rememberScrollState()
    val scrollStateSource = rememberScrollState()
    val scrollStateTarget = rememberScrollState()
    Column(
        modifier = Modifier.height(500.dp).fillMaxWidth().padding(16.dp).verticalScroll(outerScrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "In ${reverseLangMap[sourceLang]}",
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
                    .verticalScroll(scrollStateSource)
                    .padding(8.dp)
            ){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if(targetText.isNotEmpty()) sourceText else "No text detected",
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = if (!failureState && sourceText.isNotEmpty()) Color.White else Color.Red,
                    textAlign = if(sourceText.isNotEmpty()) TextAlign.Justify else TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.wrapContentSize(), horizontalArrangement = Arrangement.spacedBy(40.dp)) {
            TextRecognitionButton(image = R.drawable.copy,text = "Copy"){
                clipboardManager.setText(AnnotatedString(sourceText))
                Toast.makeText(context,"Text copied",Toast.LENGTH_SHORT).show()
            }
            TextRecognitionButton(image = R.drawable.speak, text = "Read out") {
                TTSManager.speak(sourceText)
            }
            TextRecognitionButton(image = R.drawable.share, text = "Share") {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT,sourceText)
                }
                context.startActivity(Intent.createChooser(intent,"Share text via"))
            }

        }
        //TARGET

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "In ${reverseLangMap[targetLang]}",
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
                    .verticalScroll(scrollStateTarget)
                    .padding(8.dp)
            ){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if(targetText.isNotEmpty()) targetText else "No text detected",
                    fontSize = 14.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = if (!failureState && targetText.isNotEmpty()) Color.White else Color.Red,
                    textAlign = if(sourceText.isNotEmpty()) TextAlign.Justify else TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.wrapContentSize(), horizontalArrangement = Arrangement.spacedBy(40.dp)) {
            TextRecognitionButton(image = R.drawable.copy, text = "Copy") {
                clipboardManager.setText(AnnotatedString(targetText))
                Toast.makeText(context, "Text copied", Toast.LENGTH_SHORT).show()
            }
            TextRecognitionButton(image = R.drawable.speak, text = "Read out") {
                TTSManager.speak(targetText)
            }
            TextRecognitionButton(image = R.drawable.share, text = "Share") {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT,targetText)
                }
                context.startActivity(Intent.createChooser(intent,"Share text via"))
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}
