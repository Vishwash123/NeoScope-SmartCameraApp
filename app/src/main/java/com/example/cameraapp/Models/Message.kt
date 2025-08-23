package com.example.cameraapp.Models

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.UUID


data class Message(
    val id:String = UUID.randomUUID().toString(),
    var fulltext:String="",
    var displayedText:MutableState<String> = mutableStateOf(""),
    var timestamp: Long,
    val fromUser:Boolean,
    var showShimmer:MutableState<Boolean> = mutableStateOf(true),
    var isError:MutableState<Boolean> = mutableStateOf(false),
    var prompt:String? = null,
    var image: Uri? = null,
    var responseImageUrl:String?=null,
    var promptType:String?=null,
    var requestImageUri: Uri?=null
)

