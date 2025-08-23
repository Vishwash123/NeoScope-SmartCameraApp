package com.example.cameraapp.Models

data class ChatRequest(
    val prompt:String
)

data class ChatResponse(
    val response:String? = null,
    val error:String? = null
)

data class ClassifyRequest(
    val prompt:String
)

data class ClassifyResponse(
    val response: String? = null,
    val category: String? = null,
    val score:Float? = null
)

data class ImageGenerationRequest(
    val prompt: String
)

data class ImageResponse(
    val image_url:String?=null
)


object PromptCategories{
    val promptMap = mapOf(
        "image generation request" to "image_generation",
        "background removal request" to "background_removal",
        "remove background from image" to "background_removal",
        "background transparent request" to "background_removal",
        "image editing request" to "image_editing",
        "question" to "text",
        "text command" to "text",
        "general conversation" to "text",
        "draw conclusion from data" to "text",
        "extract information from text request" to "text",
        "remove elements from text" to "text",
        "explain" to "text",
        "mental image or picture" to "text"
    )
}



