package com.example.cameraapp.Components


import android.widget.Toast
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
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.cameraapp.Models.Message
import com.example.cameraapp.R
import com.example.cameraapp.ui.theme.Montserrat

@Composable
fun UserMessageItem(modifier: Modifier=Modifier,userMessage: Message){
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val copyToastMessage = "Text Copied"

    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0f to Color(0xFF1C7DC6),
            1f to Color(0xFF3B94D9)
        )
    )


    Row(
        modifier = modifier.background(Color.Transparent).fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        Column(
            modifier = modifier.padding(16.dp).background(Color.Transparent).wrapContentSize(),

        ) {
            Box(
                modifier = modifier
                    .align(Alignment.End)
                    .wrapContentSize()
                    .background(
                        brush = gradient,
                        shape = RoundedCornerShape(13.dp)
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.wrapContentSize().background(Color.Transparent)
                ) {
                    Text(
                        text = userMessage.displayedText.value,
                        textAlign = TextAlign.Justify,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        fontFamily = Montserrat,
                        color = Color.White
                    )
                    if(userMessage.image!=null){
                        Spacer(modifier=Modifier.height(4.dp))
                        Image(
                            modifier = Modifier.size(200.dp).clip(RoundedCornerShape(16.dp)).border(width = 0.5.dp,Color.White,shape = RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop,
                            painter = rememberAsyncImagePainter(userMessage.image),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = modifier
                    .wrapContentSize()
                    .align(Alignment.End)
                    .background(
                        brush = gradient,
                        shape = RoundedCornerShape(7.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                    Image(
                        modifier = Modifier.size(16.dp).clickable {
                            clipboardManager.setText(AnnotatedString(userMessage.displayedText.value))
                            Toast.makeText(context,copyToastMessage,Toast.LENGTH_SHORT).show()
                        },
                        painter = painterResource(R.drawable.copy),
                        contentDescription = "Copy"
                    )
                    Image(
                        modifier = Modifier.size(16.dp).clickable {
                            TTSManager.speak(userMessage.displayedText.value)
                        },
                        painter = painterResource(R.drawable.speak),
                        contentDescription = "Speak"
                    )

                }
            }

        }
    }
}




