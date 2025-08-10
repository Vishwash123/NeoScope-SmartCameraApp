package com.example.cameraapp.Models

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.Detection
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class EfficientDetHelper(context:Context) {
    private val detector:ObjectDetector
    init {
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(5)
            .setScoreThreshold(0.5f)
            .build()

        val model =  org.tensorflow.lite.support.common.FileUtil.loadMappedFile(context,"1.tflite")
        detector = ObjectDetector.createFromBufferAndOptions(model,options)
    }

    fun detect(bitmap: Bitmap):List<Detection>{
        val tensorImage = TensorImage.fromBitmap(bitmap)
        return detector.detect(tensorImage)
    }
}