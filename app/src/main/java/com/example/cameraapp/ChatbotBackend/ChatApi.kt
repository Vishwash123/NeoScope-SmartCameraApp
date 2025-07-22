package com.example.cameraapp.ChatbotBackend

import com.example.cameraapp.Models.ChatRequest
import com.example.cameraapp.Models.ChatResponse
import com.example.cameraapp.Models.ClassifyRequest
import com.example.cameraapp.Models.ClassifyResponse
import com.example.cameraapp.Models.ImageGenerationRequest
import com.example.cameraapp.Models.ImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ChatApi {

    @POST("/classify")
    suspend fun classify(@Body body:ClassifyRequest):ClassifyResponse

    @POST("/chat")
    suspend fun chat(@Body request:ChatRequest) : ChatResponse

    @POST("/generate_image")
    suspend fun generateImage(@Body body: ImageGenerationRequest):ImageResponse

    @Multipart
    @POST("/remove_bg")
    suspend fun removeBackground(
        @Part image:MultipartBody.Part
    ): ImageResponse

    @Multipart
    @POST("/edit_image")
    suspend fun editImage(
        @Part image: MultipartBody.Part,
        @Part("prompt") prompt:RequestBody
    ):ImageResponse


}