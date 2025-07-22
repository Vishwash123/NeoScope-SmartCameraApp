package com.example.cameraapp.ChatbotBackend

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow

object SpeechManagerState {
    var rmsDb = MutableStateFlow(0f)
    var currentText = MutableStateFlow("")
    var isPaused = MutableStateFlow(false)
}