package com.example.cameraapp.Screens


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cameraapp.ChatbotBackend.SpeechManagerState
import com.example.cameraapp.ChatbotBackend.SpeechRecognizerManager
import com.example.cameraapp.Components.ChatBoxTextField
import com.example.cameraapp.Components.ChatsLazyColumn
import com.example.cameraapp.Components.CustomizableGradientText
import com.example.cameraapp.Components.SpeechToTextWindow
import com.example.cameraapp.Components.StopButton
import com.example.cameraapp.Navigation.ScreenSealed
import com.example.cameraapp.R
import com.example.cameraapp.ViewModels.ChatbotViewModel
import com.example.cameraapp.ui.theme.Montserrat
import kotlinx.coroutines.launch


@Composable
fun ChatbotScreen(
    modifier: Modifier=Modifier,
    navHostController: NavHostController,
    chatbotViewModel: ChatbotViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val image by chatbotViewModel.selectedImageUri

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){uri: Uri?->
        chatbotViewModel.setImageUri(uri)
    }

    var isAudioRecording by remember { mutableStateOf(false) }
    val speechManager = remember {
        SpeechRecognizerManager(
            context = context,
            onPartialResult = { text ->
            },
            onFinalResult = { resultText ->
                SpeechManagerState.currentText.value += ' ' + resultText
            },
            onError = { e ->
            },
            )
    }

    val messages = chatbotViewModel.messages
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize().alpha(if (isAudioRecording) 0.9f else 1f),
            painter = painterResource(R.drawable.chatbot_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        if (isAudioRecording) {
            SpeechToTextWindow( modifier=Modifier.align(Alignment.Center).padding(16.dp),context = LocalContext.current, onSendClicked = {
                val imageUri = chatbotViewModel.getImage();
                val currentText = SpeechManagerState.currentText.value
                coroutineScope.launch {

                    chatbotViewModel.classify(
                        context,
                        currentText,
                        imageUri
                    )

                }
                chatbotViewModel.clearImage()
                SpeechManagerState.currentText.value = ""
                SpeechManagerState.isPaused.value = false
                isAudioRecording = false

            },speechManager)
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(vertical = 20.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 60.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 20.dp
                        )
                ) {
                    Image(
                        modifier = Modifier.size(30.dp).clickable {
                            navHostController.popBackStack()
                        },
                        painter = painterResource(R.drawable.back),
                        contentDescription = null
                    )
                    Spacer(modifier.weight(1f))
                    Column(

                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomizableGradientText(

                            text = "NEO SCOPE",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = Montserrat,
                            gradientStartColor = Color(0xFFDBEBF2),
                            gradientEndColor = Color(0xFFFFFFFF),
                            dropShadowColor = Color(0xFFFFFFFF)
                        )
                        CustomizableGradientText(
                            modifier = Modifier.padding(top = 10.dp),
                            text = "CHAT WITH AI",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = Montserrat,
                            gradientStartColor = Color(0xFFDBEBF2),
                            gradientEndColor = Color(0xFFFFFFFF),
                            dropShadowColor = Color(0xFFFFFFFF)
                        )
                    }
                    Spacer(modifier.weight(1f))
                }


                Box(modifier.fillMaxSize()) {

                    ChatsLazyColumn(messages = messages, lazyState = listState,
                        onRegenerate = { id, prompt ->
                            chatbotViewModel.regenerate(id, prompt,context)
                        },
                        onImageClicked = {url->
                            navHostController.navigate(ScreenSealed.ResponseScreen.passAll(url = url))
                        }
                    )
                    ChatBoxTextField(
                        modifier.align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp).clickable(enabled = !chatbotViewModel.isGenerating){},
                        onSendClicked = { text ->
                            val imageUri = chatbotViewModel.getImage()
                            coroutineScope.launch {
                                chatbotViewModel.classify(context,text,imageUri)

                            }
                            if (chatbotViewModel.messages.isNotEmpty()) {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(chatbotViewModel.messages.lastIndex)
                                }
                            }
                            chatbotViewModel.clearImage()
                        },
                        onMicClicked = {
                            isAudioRecording = true
                            speechManager.startListening()
                            SpeechManagerState.isPaused.value = false
                        },
                        onImageUploadClicked = {
                           launcher.launch("image/*")
                        },
                        onRemoveImageClicked = {
                            chatbotViewModel.clearImage()
                        }
                        , !chatbotViewModel.isGenerating,
                        image = image
                    )
                    StopButton(
                        modifier.align(Alignment.BottomCenter)
                            .padding(bottom = 28.dp),
                        onStopClicked = {
                            chatbotViewModel.stopMessage()
                        },
                        isVisible = chatbotViewModel.isGenerating
                    )


                }


            }
        }
    }
}

