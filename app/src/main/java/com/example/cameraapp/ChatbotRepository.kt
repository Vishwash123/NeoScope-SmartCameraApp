package com.example.cameraapp


import com.example.cameraapp.ChatbotBackend.ChatApi
import com.example.cameraapp.Models.ChatRequest
import com.example.cameraapp.Models.ClassifyRequest
import com.example.cameraapp.Models.ImageGenerationRequest
import kotlinx.coroutines.CancellationException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class ChatbotRepository @Inject constructor(private val chatApi: ChatApi) {

    suspend fun classifyPrompt(prompt: String):String{
        return try {
            val response = chatApi.classify(ClassifyRequest(prompt))
            response.category ?: "No response"
        }
        catch(e:CancellationException) {
            "Stopped"
        }
        catch (e:Exception){
            "Failed"
        }
    }

    suspend fun getChatPrompt(prompt: String):String{
        return try {
            val response = chatApi.chat(ChatRequest(prompt))
            response.response ?: "No response"
        }catch(e:CancellationException) {
            "Stopped"
        }catch (e:Exception){
            "Failed"
        }
    }

    suspend fun generateImage(prompt: String):String{
        return try {
            val response = chatApi.generateImage(ImageGenerationRequest(prompt))
            response.image_url ?: "No response"
        }catch(e:CancellationException) {
            "Stopped"
        }catch (e:Exception){
            "Failed"
        }
    }

    suspend fun editImage(imageFile: File, prompt: String):String{
        return try {
            val requestImage = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestImage)

            val promptBody = prompt.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = chatApi.editImage(imagePart, promptBody)
            response.image_url ?: "No response"
        }catch(e:CancellationException) {
            "Stopped"
        }catch (e:Exception){
            "Failed"
        }
    }

    suspend fun removeBackground(imageFile: File):String{
        return try {
            val requestImage = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestImage)

            val response = chatApi.removeBackground(imagePart)
            return response.image_url ?: "No response"
        }catch(e:CancellationException) {
            "Stopped"
        }catch (e:Exception){
            "Failed"
        }
    }

    suspend fun downloadFile(url: String):Response<ResponseBody>?{
         try{
             val response = chatApi.downloadImage(url)
             return response
         }
         catch (e:Exception){
             return null
         }
    }

}