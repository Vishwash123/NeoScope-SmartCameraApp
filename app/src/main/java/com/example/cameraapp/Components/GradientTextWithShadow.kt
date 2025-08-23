package com.example.cameraapp.Components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@OptIn(ExperimentalTextApi::class)
@Composable
public fun CustomizableGradientText(
    clickable:Boolean?=null,
    navHostController: NavHostController?=null,
    route:String?=null,
    name:String?=null,
    text: String,
    modifier: Modifier = Modifier,

    fontSize: TextUnit = 14.sp,
    letterSpacing: TextUnit = 0.08.em,
    fontFamily: FontFamily? = null,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified,


    gradientStartColor: Color = Color(0xFFF2FBFF),
    gradientEndColor: Color = Color(0xFFB3D4ED),


    dropShadowColor: Color = Color(0xFF00B7FF),
    dropShadowOffsetX: Float = 0f,
    dropShadowOffsetY: Float = 0f,
    dropShadowBlurRadius: Float = 1.4f,

    strokeColor: Color = Color(0xFFFCFCFC),
    strokeWeight: Float = 0.2f
) {
    val gradientColors = listOf(gradientStartColor, gradientEndColor)
    val brush = object : ShaderBrush() {
        override fun createShader(size: androidx.compose.ui.geometry.Size): android.graphics.Shader {
            return LinearGradientShader(
                colors = gradientColors,
                from = Offset.Zero,
                to = Offset(size.width, size.height)
            )
        }
    }

    Text(
        text = text,
        modifier = if(clickable!=null) {modifier.clickable {
            if(name=="signup")
            navHostController!!.navigate(route!!)
            else navHostController!!.popBackStack()
        }} else modifier,
        style = TextStyle(
            brush = brush,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            textAlign = textAlign,
            lineHeight = lineHeight,
            shadow = Shadow(
                color = dropShadowColor,
                offset = Offset(dropShadowOffsetX, dropShadowOffsetY),
                blurRadius = dropShadowBlurRadius
            )
        ),
    )
}


