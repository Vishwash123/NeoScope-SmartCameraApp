package com.example.cameraapp.Components

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale



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
