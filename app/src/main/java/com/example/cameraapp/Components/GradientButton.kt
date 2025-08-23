package com.example.cameraapp.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cameraapp.ui.theme.Montserrat

@Composable
fun GradientButton(
    text:String,
    modifier: Modifier = Modifier,
    onClick:()->Unit
){
    val buttonGradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF3FA9DA),
            1.0f to Color(0xFF66B1E5)
        )
    )
    
    val textGradient = Brush.horizontalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFFF2FBFF),
            1.0f to Color(0xFFB3D4ED)
        )
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = buttonGradient
            )
            .clickable ( onClick = onClick )
            .padding(vertical = 15.dp, horizontal = 44.dp),
    contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            style = TextStyle(
                drawStyle = Stroke(0.2f)
            ),
            fontSize = 20.sp,
            letterSpacing = 1.6.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Montserrat

        )
        Text(
            text = text,
            style = TextStyle(
                brush = textGradient
            ),
            fontSize = 20.sp,
            letterSpacing = 1.6.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Montserrat

        )
    }
}

