package com.example.cameraapp.ChatbotBackend

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import java.util.Locale

class SpeechRecognizerManager(
    private val context: Context,
    private val onPartialResult: (String) -> Unit,
    private val onFinalResult: (String) -> Unit,
    private val onError: (String) -> Unit
) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)  // Enable partial results
    }


    fun startListening() {
        if(!SpeechManagerState.isPaused.value) {
            Toast.makeText(context,"Listening Started",Toast.LENGTH_SHORT).show()
            SpeechManagerState.currentText.value = ""
        }
        if (speechRecognizer == null) {
            Toast.makeText(context,"Listening Started",Toast.LENGTH_SHORT).show()

//            Toast.makeText(waveDb.context,"Instantiated", Toast.LENGTH_SHORT).show()

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        matches?.firstOrNull()?.let { onFinalResult(it) }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        matches?.firstOrNull()?.let { onPartialResult(it) }
                    }

                    override fun onError(error: Int) {
                        onError("Error code: $error")
                    }

                    override fun onReadyForSpeech(params: Bundle?) {}
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {
                        Toast.makeText(context,"rms:$rmsdB",Toast.LENGTH_SHORT).show()
                        SpeechManagerState.rmsDb.value = rmsdB
                    }
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        }
        speechRecognizer?.startListening(recognizerIntent)
    }

    fun stopListening() {
        Toast.makeText(context,"Listening Stopped",Toast.LENGTH_SHORT).show()

        speechRecognizer?.stopListening()
    }

    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}