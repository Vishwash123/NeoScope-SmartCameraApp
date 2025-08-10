package com.example.cameraapp.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cameraapp.Models.Message

@Composable
fun ChatsLazyColumn(modifier: Modifier=Modifier,messages:List<Message>,lazyState:LazyListState,onRegenerate:(String,String)->Unit,onImageClicked:(String?)->Unit){
    LazyColumn(state = lazyState,modifier = Modifier.fillMaxSize().background(Color.Transparent),
        contentPadding = PaddingValues(bottom = 170.dp)
    ) {
        items(messages,key={it.id}){message->
            if(message.fromUser){
                UserMessageItem(userMessage = message)
            }
            else{
                AiMessageItem(aiMessage = message,onRegenerate = onRegenerate, onImageClicked = onImageClicked)
            }

        }
    }
}