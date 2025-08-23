package com.example.cameraapp.Components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cameraapp.ChatbotBackend.SpeechManagerState
import com.example.cameraapp.ChatbotBackend.SpeechRecognizerManager
import com.example.cameraapp.R
import com.example.cameraapp.ui.theme.Montserrat

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SpeechToTextWindow(
    modifier: Modifier=Modifier,
    context: Context = LocalContext.current,
    onSendClicked:()->Unit,
    speechManager:SpeechRecognizerManager
){
    val currentText by SpeechManagerState.currentText.collectAsState()

    val isPaused by SpeechManagerState.isPaused.collectAsState()
    Column(
        modifier=modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val scrollState = rememberScrollState()
        Spacer(modifier=modifier.height(60.dp))
        Text(
            modifier = modifier,
            textAlign = TextAlign.Center,
            text = "Say anything...",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            fontFamily = Montserrat,
            color = Color.White
        )
        Spacer(modifier=modifier.width(40.dp))
        MicWaveWithRms(modifier = modifier,SpeechManagerState.rmsDb.value)
        Spacer(modifier=Modifier.height(8.dp))
        Text(
            modifier = Modifier.wrapContentSize().weight(1f).fillMaxWidth().height(240.dp)
                .verticalScroll(scrollState)
                .background(Color.Cyan.copy(alpha = 0.1f), shape = RoundedCornerShape(16.dp)).padding(16.dp),
            text=currentText,
            color = Color.White,
            fontSize = 14.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Normal
        )


        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                modifier = modifier.clickable {
                    if(isPaused){

                        speechManager.startListening()
                        SpeechManagerState.isPaused.value = false

                    }
                    else{

                        speechManager.stopListening()
                        SpeechManagerState.isPaused.value = true
                    }
                }.size( if(isPaused)80.dp else 80.dp),
                painter = painterResource(if(isPaused) R.drawable.start_stt else R.drawable.puase_stt),
                contentDescription = null
            )
            Spacer(modifier = modifier.width(50.dp))
            Image(
                modifier = Modifier.clickable {
                    onSendClicked()
                }.size(85.dp).rotate(45f),
                painter = painterResource(R.drawable.send_button),
                contentDescription = null
            )

        }
    }
}


