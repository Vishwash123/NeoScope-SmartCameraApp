package com.example.cameraapp

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast

object ImageHelper {
    fun saveToGallery(context: Context, uri: Uri){
        val resolver = context.contentResolver
        val inputStream = resolver.openInputStream(uri)?:return
        val filename = "neoscope_${System.currentTimeMillis()}.jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,filename)
            put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val outputUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
        outputUri?.let {
            resolver.openOutputStream(it)?.use { outputStream->
                inputStream.copyTo(outputStream)
            }
        }

        inputStream.close()
        Toast.makeText(context,"Image saved to device", Toast.LENGTH_SHORT).show()
    }
}