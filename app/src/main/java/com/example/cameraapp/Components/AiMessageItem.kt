package com.example.cameraapp.Components

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.cameraapp.Models.Message
import com.example.cameraapp.R
import com.example.cameraapp.ui.theme.Montserrat
import com.valentinilk.shimmer.shimmer

@Composable
fun AiMessageItem(modifier: Modifier=Modifier,aiMessage: Message,onRegenerate:(String,String)->Unit,onImageClicked:(String?)->Unit){
    if(aiMessage.showShimmer.value){
        AiShimmerPlaceholder()
    }
    else{
        ActualAiMessageItem(modifier,aiMessage,onRegenerate,onImageClicked)
    }
}

@Composable
fun AiShimmerPlaceholder(){
    Row(
        modifier = Modifier.shimmer()
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray),
            )
            Box(
                modifier = Modifier
                    .size(120.dp, 20.dp)
                    .background(Color.LightGray),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .background(Color.LightGray),
            )
        }
    }
}

@Composable
fun ActualAiMessageItem(modifier: Modifier=Modifier,aiMessage: Message,onRegenerate: (String, String) -> Unit,onImageClicked: (String?) -> Unit){
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val copyToastMessage = "Text Copied"


    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0f to Color(0xFF61A4E2),
            1f to Color(0xFF307199)
        )
    )

    Column(
        modifier = modifier.padding(16.dp).background(Color.Transparent).wrapContentSize(),
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .animateContentSize()
                .background(
                    brush = gradient,
                    shape = RoundedCornerShape(13.dp)
                )
                .padding(16.dp)
        ){
            if(aiMessage.responseImageUrl!=null){
                Image(
                    modifier = Modifier.size(200.dp).clickable {
                        onImageClicked(aiMessage.responseImageUrl)
                    }.clip(RoundedCornerShape(16.dp)).border(width = 0.5.dp,Color.White, shape = RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    painter = rememberAsyncImagePainter(aiMessage.responseImageUrl),
                    contentDescription = null
                )
            }

            Text(
                text = if(!aiMessage.showShimmer.value) aiMessage.displayedText.value else "Generating...",
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                fontFamily = Montserrat,
                color = if(!aiMessage.isError.value)Color.White else Color.Red
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    brush = gradient,
                    shape = RoundedCornerShape(7.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ){
            Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                Image(modifier = Modifier.size(16.dp).clickable {
                    clipboardManager.setText(AnnotatedString(aiMessage.displayedText.value))
                    Toast.makeText(context,copyToastMessage,Toast.LENGTH_SHORT).show() },painter = painterResource(R.drawable.copy), contentDescription = "Copy")
                Image(modifier = Modifier.size(16.dp).clickable{
                    TTSManager.speak(aiMessage.displayedText.value)
                },painter = painterResource(R.drawable.speak), contentDescription = "Speak")
                Image(modifier = Modifier.size(16.dp).clickable {
                    onRegenerate(aiMessage.id,aiMessage.prompt!!)
                },painter = painterResource(R.drawable.regenerate), contentDescription = "Regenerate")
            }
        }
    }
}

