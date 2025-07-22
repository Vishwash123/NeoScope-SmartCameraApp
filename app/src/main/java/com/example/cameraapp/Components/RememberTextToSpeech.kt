package com.example.cameraapp.Components

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale


//@Composable
//fun RememberTextToSpeech():TextToSpeech {
//    val context = LocalContext.current
//    val tts = remember {
//        TextToSpeech(context, null)
//    }
//
//    LaunchedEffect(tts) {
//        val result = tts.setLanguage(Locale.ENGLISH)
//        if(result==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
//            Toast.makeText(context,"Language not supported",Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            tts.stop()
//            tts.shutdown()
//        }
//    }
//    return tts;
//}
object TTSManager {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    fun init(context: Context, onReady: (() -> Unit)? = null) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale.ENGLISH)
                    isInitialized = result != TextToSpeech.LANG_MISSING_DATA &&
                            result != TextToSpeech.LANG_NOT_SUPPORTED
                    onReady?.invoke()
                }
            }
        }
    }

    fun speak(text: String) {
        if (isInitialized) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            Log.w("TTSManager", "TTS not initialized yet")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
