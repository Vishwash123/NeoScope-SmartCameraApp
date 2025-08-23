package com.example.cameraapp.Components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cameraapp.R

@Composable
fun FlashToggle(
    isFlashOn:Boolean,
    onToggle:(Boolean)->Unit
){
    val transition = updateTransition(targetState = isFlashOn, label = "FlashToggle")

    val flashOnColor by transition.animateColor(label = "FlashOnColor"){state->
        if (!state) Color(0xFFFFD54F) else Color.Gray.copy(alpha = 0.2f)
    }

    val flashOffColor by transition.animateColor(label = "FlashOffColor") {state->
        if (!state)  Color.Gray.copy(alpha = 0.2f)  else Color(0xFFFFD54F)
    }


    Row(
        modifier = Modifier
            .width(150.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(50))
            .border(1.dp,shape = RoundedCornerShape(50), color = Color.White)
            .background(Color.Gray.copy(alpha = 0.15f))
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10))
                .background(flashOnColor)
                .clickable { onToggle(false) }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.flash_off),
                contentDescription = null
            )
        }


        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(10))
                .background(flashOffColor)
                .clickable { onToggle(true) }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.flash_on),
                contentDescription = null
            )
        }
    }
}

