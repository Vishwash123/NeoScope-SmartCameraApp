package com.example.cameraapp.Components


import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.cameraapp.ui.theme.Montserrat


@Composable
fun LineWithText(){


    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
        Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(1.dp).background(Color(0xFFFFFFFF)))
        CustomizableGradientText(
            text = "OR",
            fontSize = 14.sp,
            letterSpacing = 0.08.em,
            gradientStartColor = Color(0xFFF2FBFF),
            gradientEndColor = Color(0xFFB3D4ED),
            dropShadowColor = Color(0xFF00B7FF),
            dropShadowBlurRadius = 1.4f,
            dropShadowOffsetX = 0f,
            dropShadowOffsetY = 0f,
            strokeColor = Color(0xFFFCFCFC),
            strokeWeight = 0.2f,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold
        )




        Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(1.dp).background(Color(0xFFFFFFFF)))

    }

}

