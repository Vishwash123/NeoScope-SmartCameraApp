package com.example.cameraapp.ViewModels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraapp.ChatbotBackend.BASE_URL
import com.example.cameraapp.ChatbotRepository
import com.example.cameraapp.ImageHelper
import com.example.cameraapp.Models.Message
import com.example.cameraapp.Models.PromptCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@HiltViewModel
class ChatbotViewModel @Inject constructor(private val chatbotRepository: ChatbotRepository) :ViewModel() {
    private val _messages = mutableStateListOf<Message>()
    val messages:List<Message> get() = _messages
    var isGenerating by mutableStateOf(false)
        private set

    private var _selectedImageUri = mutableStateOf<Uri?>(null)
        private set

    var selectedImageUri:State<Uri?> = _selectedImageUri

    fun setImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun isImageAvailable():Boolean{
        return _selectedImageUri.value!=null
    }

    fun getImage():Uri?{
        return _selectedImageUri.value
    }

    fun clearImage(){
        _selectedImageUri.value = null
    }

    private var chatJob: Job? = null
    private var classifyJob:Job? = null



    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
            tempFile.outputStream().use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun sendNoInstructionResponse(){
        val responseMessage = Message(timestamp = System.currentTimeMillis(), fromUser = false, prompt = "No instructions provided")
        addMessage(responseMessage)
        val lastIndex = _messages.lastIndex
        _messages[lastIndex].responseImageUrl = null
        _messages[lastIndex].showShimmer.value = false
        _messages[lastIndex].isError.value = true
        _messages[lastIndex].displayedText.value = "No instructions provided"

    }

    fun sendFailedRepsonse(action: String){
        val lastIndex = _messages.lastIndex
        _messages[lastIndex].promptType = action
        _messages[lastIndex].responseImageUrl = null
        _messages[lastIndex].showShimmer.value = false
        _messages[lastIndex].isError.value = true
        _messages[lastIndex].displayedText.value = if(action =="Failed") "Failed to fetch. Server not found." else "Message Generation was Stopped"
    }

    suspend fun classify(context: Context,prompt: String,imageUri: Uri?){
        isGenerating = true
        val userMessage = Message(fulltext = prompt, timestamp = System.currentTimeMillis(), fromUser = true, image = imageUri)
        userMessage.displayedText.value = prompt
        addMessage(userMessage)
        val userIndex = _messages.lastIndex
        if(prompt.isEmpty()){
            sendNoInstructionResponse()
            isGenerating=false
            return
        }

        val responseMessage = Message(timestamp = System.currentTimeMillis(), fromUser = false, prompt = prompt)
        addMessage(responseMessage)
        classifyJob = viewModelScope.launch {
            val category = chatbotRepository.classifyPrompt(prompt)
            val action = PromptCategories.promptMap[category]
            if (action == null || action == "Failed" || action=="Stopped") {
                sendFailedRepsonse(action ?: "Failed")
                responseMessage.showShimmer.value = false
                isGenerating = false
                return@launch
            }
            _messages[_messages.lastIndex].promptType = action

            sendMessage(prompt, imageUri, action!!, context)
        }
    }





    fun sendMessage(text:String,image:Uri?=null,action:String,context: Context){
        val lastIndex = _messages.lastIndex
        chatJob = viewModelScope.launch {
            try{
                when(action){
                    "image_generation"->{

                            val result = chatbotRepository.generateImage(text)
                            val url = BASE_URL + result
                            _messages[lastIndex].responseImageUrl = url
                            _messages[lastIndex].showShimmer.value = false;

                    }
                    "background_removal"->{
                        if(image!=null) {
                            _messages[lastIndex].requestImageUri = image
                            val imageFile = getFileFromUri(context, image)
                            val result = chatbotRepository.removeBackground(imageFile!!)
                            val url = BASE_URL + result
                            clearImage()
                            _messages[lastIndex].responseImageUrl = url
                            _messages[lastIndex].showShimmer.value = false;
                        }
                        else{
                        }
                    }
                    "image_editing"->{
                        if(image!=null) {
                            val imageFile = getFileFromUri(context, image)
                            val result = chatbotRepository.editImage(imageFile!!,text)
                            val url = BASE_URL + result
                            _messages[lastIndex].requestImageUri = image
                            _messages[lastIndex].responseImageUrl = url
                            _messages[lastIndex].showShimmer.value = false;
                        }
                    }
                    "text"->{
                        val result = chatbotRepository.getChatPrompt(prompt = text)
                        _messages[lastIndex].fulltext = result;
                        _messages[lastIndex].showShimmer.value = false;
                        _messages[lastIndex].displayedText.value = cleanResponse(_messages[lastIndex].displayedText.value )


                        for (i in result.indices) {
                            delay(20)
                            _messages[lastIndex].displayedText.value = result.substring(0, i + 1)
                        }
                    }
                }


            }
            catch (e:CancellationException){

                        _messages[lastIndex].responseImageUrl = null
                        _messages[lastIndex].showShimmer.value = false
                        _messages[lastIndex].fulltext = ""
                        _messages[lastIndex].displayedText.value += "\nMessage Generation was Stopped"




            }
            catch (e:Exception){
                _messages[lastIndex].responseImageUrl = null
                _messages[lastIndex].copy(
                    fulltext = "Failed to Fetch",


                )
                _messages[lastIndex].isError.value = true
                _messages[lastIndex].displayedText.value = "Failed to Fetch + ${e.message}"
                _messages[lastIndex].showShimmer.value = false
            }
            finally {
                isGenerating=false
                chatJob = null
                classifyJob = null
            }
        }
    }

    fun regenerate(messageId:String,prompt:String,context: Context){
        isGenerating = true
        val itemIndex = _messages.indexOfFirst { it.id == messageId }

        _messages[itemIndex].let{
            it.fulltext=""
            it.timestamp = System.currentTimeMillis()
        }
        val previousText = _messages[itemIndex].displayedText.value
        _messages[itemIndex].showShimmer.value = true;
        _messages[itemIndex].displayedText.value = "";
        _messages[itemIndex].responseImageUrl = null

        val action = _messages[itemIndex].promptType
        if(action==null || previousText == "Failed to fetch. Server not found."){
            _messages[itemIndex].showShimmer.value = false;
            _messages[itemIndex].displayedText.value = previousText
            _messages[itemIndex].isError.value = true
            isGenerating = false
            return
        }
        chatJob = viewModelScope.launch {
            try{
                when(action){
                    "image_generation"->{

                        val result = chatbotRepository.generateImage(prompt)
                        val url = BASE_URL + result
                        _messages[itemIndex].responseImageUrl = url
                        _messages[itemIndex].showShimmer.value = false;

                    }
                    "background_removal"->{
                            val imageUri = _messages[itemIndex].requestImageUri
                            val imageFile = getFileFromUri(context,imageUri!!)
                            val result = chatbotRepository.removeBackground(imageFile!!)
                            val url = BASE_URL + result
                            _messages[itemIndex].responseImageUrl = url
                            _messages[itemIndex].showShimmer.value = false;

                    }
                    "image_editing"->{
                            val imageUri = _messages[itemIndex].requestImageUri
                            val imageFile = getFileFromUri(context, imageUri!!)
                            val result = chatbotRepository.editImage(imageFile!!,prompt)
                            val url = BASE_URL + result
                            _messages[itemIndex].responseImageUrl = url
                            _messages[itemIndex].showShimmer.value = false;

                    }
                    "text"->{
                        val result = chatbotRepository.getChatPrompt(prompt = prompt)
                        _messages[itemIndex].fulltext = result;
                        _messages[itemIndex].showShimmer.value = false;
                        _messages[itemIndex].responseImageUrl = null
                        _messages[itemIndex].displayedText.value = cleanResponse(_messages[itemIndex].displayedText.value )




                        for (i in result.indices) {
                            delay(20)
                            _messages[itemIndex].displayedText.value = result.substring(0, i + 1)
                        }
                    }
                }

            }
            catch (e:Exception){
                isGenerating = false
                _messages[itemIndex].copy(
                    fulltext = "Failed to Fetch",


                    )
                _messages[itemIndex].isError.value = true
                _messages[itemIndex].displayedText.value = "Failed to Fetch + ${e.message}"
                _messages[itemIndex].showShimmer.value = false
            }
            finally {
                isGenerating = false
                chatJob = null

            }
        }

    }

    fun addMessage(message: Message){
        _messages.add( message)
        _messages.sortBy { it.timestamp }
    }

    fun stopMessage(){
         classifyJob?.cancel()
         chatJob?.cancel()
    }

    fun dowloadImage(context: Context,url:String){
        viewModelScope.launch {
            val response = chatbotRepository.downloadFile(url)
            if(response==null){
                return@launch
            }
            else{
                val fileName = "neoscope_${System.currentTimeMillis()}.jpg"
                val file = File(context.cacheDir, fileName)

                response.body()?.byteStream()?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }

                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                ImageHelper.saveToGallery(context,uri)
            }
        }
    }

    fun cleanResponse(text: String): String {
        return text
            .replace(Regex("\\(.*?\\)|\\{.*?\\}|\\|.*?\\||<\\|.*?\\|>|<.*?>"), "")
            .replace(Regex("\\[[0-9]+m\\]"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }


}



