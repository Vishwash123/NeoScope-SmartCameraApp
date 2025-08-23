package com.example.cameraapp.Components



import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cameraapp.ChatbotBackend.SpeechRecognizerManager
import com.example.cameraapp.R
import com.example.cameraapp.ui.theme.Montserrat

@Composable
fun ChatBoxTextField(
    modifier: Modifier = Modifier,
    onSendClicked: (String) -> Unit = {},
    onMicClicked: () -> Unit,
    onImageUploadClicked:()->Unit,
    onRemoveImageClicked:()->Unit,
    isVisible: Boolean,
    image: Uri?=null
) {
    val gradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0f to Color(0xFF7BB1C0),
            1f to Color(0xFF257BB7)
        )
    )
    var text by remember { mutableStateOf("") }
    val verticalScrollState = rememberScrollState()
    val colors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent
    )

    val alpha by animateFloatAsState(if(isVisible) 1f else 0f)
    val offsetY by animateFloatAsState(if(isVisible) 0f else 100f)

    Box(
            modifier = modifier
                .graphicsLayer {
                    this.alpha = alpha
                    this.translationY = offsetY
                }
                .padding(12.dp)
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    brush = gradient,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp)
                ).clickable(enabled = isVisible){}

        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth()
                        .heightIn(50.dp, 58.dp)
                        .verticalScroll(verticalScrollState),
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(
                            modifier = Modifier.alpha(0.8f),
                            text = "Type a Message",
                            color = Color.White,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Justify
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    ),
                    maxLines = 10,
                    singleLine = false,
                    colors = colors,

                    )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(image==null) {
                        Image(
                            modifier = Modifier.padding(start = 16.dp).size(34.dp)
                                .clickable {
                                    onImageUploadClicked()
                                },
                            painter = painterResource(R.drawable.upload_image),
                            contentDescription = null
                        )
                    }
                    else{
                        ImagePreview(
                            modifier = Modifier.padding(start = 16.dp),
                            onRemoveClicked = onRemoveImageClicked,
                            image

                        )
                    }

                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp).wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(30.dp).clickable {
                                onMicClicked()

                            },
                            painter = painterResource(R.drawable.microphone),
                            contentDescription = null
                        )

                        Image(
                            modifier = Modifier
                                .size(52.dp)
                                .rotate(45f)
                                .clickable {
                                    onSendClicked(text)
                                    text = ""
                                },
                            painter = painterResource(R.drawable.send_button),
                            contentDescription = null
                        )
                    }


                }
            }
        }


}

