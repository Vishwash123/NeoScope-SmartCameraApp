package com.example.cameraapp.Models

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.checkerframework.checker.mustcall.qual.MustCallAlias
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



//object MessageSamples{
//    val sampleMessages = listOf(
//        Message(
//            text = "Hey, how are you?",
//            timestamp = 1_000L,
//            fromUser = true
//        ),
//        Message(
//            text = "Hello! I'm doing really well, thank you for asking. It's always a pleasure to chat with someone. I'm here to assist you with whatever you need, whether it's answering questions, solving problems, or just having a friendly conversation. What would you like to talk about today?",
//            timestamp = 2_000L,
//            fromUser = false
//        ),
//        Message(
//            text = "Can you help me with my math homework?",
//            timestamp = 3_000L,
//            fromUser = true
//        ),
//        Message(
//            text = "Absolutely, I'd love to help you out with your math homework. Mathematics can be tricky at times, but with a little explanation and practice, it becomes much more manageable. Just let me know which topic you're currently working on, and we’ll go through it step-by-step together.",
//            timestamp = 4_000L,
//            fromUser = false
//        ),
//        Message(
//            text = "It's about solving quadratic equations.",
//            timestamp = 5_000L,
//            fromUser = true
//        ),
//        Message(
//            text = "Great! Quadratic equations are a classic topic in algebra. They generally take the form ax² + bx + c = 0, where a, b, and c are constants. There are multiple methods to solve them: factoring, completing the square, or using the quadratic formula. The method you choose usually depends on the specific equation you're working with. If you give me an example, I can walk you through the steps in detail.",
//            timestamp = 6_000L,
//            fromUser = false
//        )
//    )
//
//}