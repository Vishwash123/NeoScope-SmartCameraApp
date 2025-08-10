package com.example.cameraapp.ViewModels

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraapp.ChatbotBackend.BASE_URL
import com.example.cameraapp.ChatbotBackend.ChatApi
import com.example.cameraapp.ChatbotBackend.NetworkModule
import com.example.cameraapp.ChatbotRepository
import com.example.cameraapp.ImageHelper
import com.example.cameraapp.Models.ChatRequest
import com.example.cameraapp.Models.Message
import com.example.cameraapp.Models.PromptCategories
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@HiltViewModel
class ChatbotViewModel @Inject constructor(private val chatbotRepository: ChatbotRepository) :ViewModel() {
//    private val _messages = mutableStateOf<List<Message>>(emptyList())
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
//    var currentlyProcessedMessageId:String? = null

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
//        val responseMessage = Message(timestamp = System.currentTimeMillis(), fromUser = false, prompt = "Failed to fetch")
//        addMessage(responseMessage)
        val lastIndex = _messages.lastIndex
        _messages[lastIndex].promptType = action
        _messages[lastIndex].responseImageUrl = null
        _messages[lastIndex].showShimmer.value = false
        _messages[lastIndex].isError.value = true
        _messages[lastIndex].displayedText.value = if(action =="Failed") "Failed to fetch. Server not found." else "Message Generation was Stopped"
    }

    suspend fun classify(context: Context,prompt: String,imageUri: Uri?){
//        Toast.makeText(context,"classify triggered",Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "$category", Toast.LENGTH_SHORT).show()
            val action = PromptCategories.promptMap[category]
            Toast.makeText(context, "$category", Toast.LENGTH_SHORT).show()
            if (action == null || action == "Failed" || action=="Stopped") {
                sendFailedRepsonse(action ?: "Failed")
                responseMessage.showShimmer.value = false
                isGenerating = false
                return@launch
            }
//        _messages[userIndex].promptType = action
            _messages[_messages.lastIndex].promptType = action

            sendMessage(prompt, imageUri, action!!, context)
        }
    }





    fun sendMessage(text:String,image:Uri?=null,action:String,context: Context){

//        val userMessage = Message(fulltext = text, timestamp = System.currentTimeMillis(), fromUser = true, image = image, promptType = action)
//        userMessage.displayedText.value = text
//
//        addMessage(userMessage)

//        currentlyProcessedMessageId = responseMessage.id



        val lastIndex = _messages.lastIndex
        chatJob = viewModelScope.launch {
            try{

//                val result = ChatbotRetrofitInstance.api.chat(ChatRequest(text))
//                val fullText = result.response ?: "No response"
                when(action){
                    "image_generation"->{

                            val result = chatbotRepository.generateImage(text)
                            //get
                            val url = BASE_URL + result
                            _messages[lastIndex].responseImageUrl = url
                            _messages[lastIndex].showShimmer.value = false;

                    }
                    "background_removal"->{
                        if(image!=null) {
                            _messages[lastIndex].requestImageUri = image
                            val imageFile = getFileFromUri(context, image)
                            val result = chatbotRepository.removeBackground(imageFile!!)
                            //get
                            val url = BASE_URL + result
                            clearImage()
                            _messages[lastIndex].responseImageUrl = url
                            _messages[lastIndex].showShimmer.value = false;
                        }
                        else{
                            Toast.makeText(context,"image is null",Toast.LENGTH_SHORT).show()
                        }
                    }
                    "image_editing"->{
                        if(image!=null) {
                            val imageFile = getFileFromUri(context, image)
                            val result = chatbotRepository.editImage(imageFile!!,text)
                            //get
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


                        for (i in result.indices) {
                            delay(20) // Typing speed
                            _messages[lastIndex].displayedText.value = result.substring(0, i + 1)
                        }
                    }
                }


//                currentlyProcessedMessageId = null
//                isGenerating = false;

            }
            catch (e:CancellationException){

                        _messages[lastIndex].responseImageUrl = null
                        _messages[lastIndex].showShimmer.value = false
                        _messages[lastIndex].fulltext = ""
                        _messages[lastIndex].displayedText.value += "\nMessage Generation was Stopped"




            }
            catch (e:Exception){
//                isGenerating = false
//                currentlyProcessedMessageId = null
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
//                currentlyProcessedMessageId = null
            }
        }
    }

    fun regenerate(messageId:String,prompt:String,context: Context){
//        currentlyProcessedMessageId = messageId
//        Toast.makeText(context,"Regen called",Toast.LENGTH_SHORT).show()
        Toast.makeText(context,"$isGenerating",Toast.LENGTH_SHORT).show()
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
        Toast.makeText(context,"$action",Toast.LENGTH_SHORT).show()
        viewModelScope.launch {
            try{
                when(action){
                    "image_generation"->{

                        val result = chatbotRepository.generateImage(prompt)
                        //get
                        val url = BASE_URL + result
                        _messages[itemIndex].responseImageUrl = url
                        _messages[itemIndex].showShimmer.value = false;

                    }
                    "background_removal"->{
                            val imageUri = _messages[itemIndex].requestImageUri
                            val imageFile = getFileFromUri(context,imageUri!!)
                            val result = chatbotRepository.removeBackground(imageFile!!)
                            //get
                            val url = BASE_URL + result
                            _messages[itemIndex].responseImageUrl = url
                            _messages[itemIndex].showShimmer.value = false;

                    }
                    "image_editing"->{
                            val imageUri = _messages[itemIndex].requestImageUri
                            val imageFile = getFileFromUri(context, imageUri!!)
                            val result = chatbotRepository.editImage(imageFile!!,prompt)
                            //get
                            val url = BASE_URL + result
                            _messages[itemIndex].responseImageUrl = url
                            _messages[itemIndex].showShimmer.value = false;

                    }
                    "text"->{
                        Toast.makeText(context,"Regen text",Toast.LENGTH_SHORT).show()
                        val result = chatbotRepository.getChatPrompt(prompt = prompt)
                        _messages[itemIndex].fulltext = result;
                        _messages[itemIndex].showShimmer.value = false;
                        _messages[itemIndex].responseImageUrl = null



                        for (i in result.indices) {
                            delay(20) // Typing speed
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
//        isGenerating = false
         chatJob?.cancel()
    }

    fun dowloadImage(context: Context,url:String){
        viewModelScope.launch {
            val response = chatbotRepository.downloadFile(url)
            if(response==null){
                Toast.makeText(context,"Download failed",Toast.LENGTH_SHORT).show()
                return@launch
            }
            else{
                val fileName = "neoscope_${System.currentTimeMillis()}.jpg"
                val file = File(context.cacheDir, fileName)

// Step 1: Write downloaded bytes to file
                response.body()?.byteStream()?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }

// Step 2: Convert File to Uri
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                ImageHelper.saveToGallery(context,uri)
//                val saved = ImageHelper.saveToGallery(context,)
            }
        }
    }


}



