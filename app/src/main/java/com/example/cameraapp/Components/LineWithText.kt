package com.example.cameraapp.Components


import CustomizableGradientText
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.cameraapp.ui.theme.Montserrat


@Composable
fun LineWithText(){

//    val gradient = Brush.horizontalGradient(
//        colorStops = arrayOf(
//            0.0f to Color(0xFFF2FBFF),
//            1.0F to Color(0xFFB3D4ED)
//        )
//    )
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()){
        Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(1.dp).background(Color(0xFFFFFFFF)))

//        Text(
//            text = "OR",
//            fontFamily = Montserrat,
//            fontWeight = FontWeight.Bold,
//            fontSize = 14.sp,
//            letterSpacing = 1.2.sp,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.padding(8.dp),
//            style = TextStyle(
//                brush = gradient
//            )
//        )

//        GradientStrokedText(
//            text = "LOGIN",
//            fontSize = 20.sp,
//            strokeColor = Color(0xFFFCFCFC),
//            strokeWidth = 0.2f,
//            gradientStops = arrayOf(
//                0.0f to Color(0xFFF2FBFF),  // Start color at 0%
//                1.0f to Color(0xFFB3D4ED)   // End color at 100%
//            ),
//            modifier = Modifier.padding(16.dp)
//        )

//
//        val gradient = arrayOf(
//            0.0f to Color(0xFF6AC4ED),
//            1.0f to Color(0xFF4285B7)
//        )
////
//        GradientShadowText(
//            text = "LOGIN",
//            fontSize = 10.sp,
//            letterSpacing = 8.sp, // note: letterSpacing is not handled in this Canvas version
//            shadowColor = Color(0xFF00B7FF),
//            shadowBlurRadius = 8f,
//            shadowOffsetX = 0f,
//            shadowOffsetY = 0f,
//            gradientStops = gradient,
//            modifier = Modifier.padding(horizontal = 16.dp)
//        )


        CustomizableGradientText(
            text = "OR",
            fontSize = 14.sp,
            letterSpacing = 0.08.em, // 8%
            gradientStartColor = Color(0xFFF2FBFF),
            gradientEndColor = Color(0xFFB3D4ED),
            dropShadowColor = Color(0xFF00B7FF),
            dropShadowBlurRadius = 1.4f,
            dropShadowOffsetX = 0f,
            dropShadowOffsetY = 0f,
            strokeColor = Color(0xFFFCFCFC), // Figma stroke color (for reference)
            strokeWeight = 0.2f, // Figma stroke weight (for reference)
            // Example of changing font family and font weight
            fontFamily = Montserrat, // Replace with your actual imported font family
            fontWeight = FontWeight.Bold // Example font weight
        )




        Box(modifier = Modifier.weight(1f).padding(horizontal = 8.dp).height(1.dp).background(Color(0xFFFFFFFF)))

    }
//
}
//
//
//@Composable
//fun GradientShadowText(
//    text: String,
//    modifier: Modifier = Modifier,
//    fontSize: TextUnit = 20.sp,
//    letterSpacing: TextUnit = 1.6.sp,
//    shadowColor: Color = Color(0xFF00B7FF),
//    shadowBlurRadius: Float = 8f,  // blur radius like spread in figma
//    shadowOffsetX: Float = 0f,
//    shadowOffsetY: Float = 0f,
//    gradientStops: Array<Pair<Float, Color>>,
//) {
//    val density = LocalDensity.current
//    val fontSizePx = with(density) { fontSize.toPx() }
//    val context = LocalContext.current
//
//    val typeface = remember {
//        ResourcesCompat.getFont(context, R.font.kumbh_sans) ?: Typeface.DEFAULT
//    }
//
//    Canvas(modifier = modifier.fillMaxWidth()) {
//        val nativeCanvas = drawContext.canvas.nativeCanvas
//
//        val paint = android.graphics.Paint().apply {
//            isAntiAlias = true
//            style = android.graphics.Paint.Style.FILL
//            textSize = fontSizePx
//            textAlign = android.graphics.Paint.Align.LEFT
//            this.typeface = typeface
//
//            // Gradient shader
//            val (positions, colors) = gradientStops.unzip()
//            shader = LinearGradient(
//                0f, 0f, size.width, 0f,
//                colors.map { it.toArgb() }.toIntArray(),
//                positions.toFloatArray(),
//                Shader.TileMode.CLAMP
//            )
//
//            // Drop shadow with offset and blur
//            setShadowLayer(shadowBlurRadius, shadowOffsetX, shadowOffsetY, shadowColor.toArgb())
//        }
//
//        val baseline = fontSizePx
//        nativeCanvas.drawText(text, 0f, baseline, paint)
//    }
//}
//
//
//
//@Preview
//@Composable
//fun LineWithTextPreview(){
//    LineWithText()
//}


//
//package com.example.cameraapp
//
//import CustomizableGradientText
//import android.graphics.LinearGradient
//import android.graphics.Shader
//import android.graphics.Typeface
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.nativeCanvas
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.em
//import androidx.compose.ui.unit.sp
//import androidx.core.content.res.ResourcesCompat
//import com.example.cameraapp.ui.theme.Montserrat
//
//@Composable
//fun LineWithText() {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        // Left line
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 8.dp)
//                .height(1.dp)
//                .background(Color(0xFFFFFFFF))
//        )
//
//        Spacer(modifier = Modifier.width(8.dp))
//        CustomizableGradientText(
//            text = "OR",
//            fontSize = 10.sp,
//            letterSpacing = 0.08.em, // 8%
//            gradientStartColor = Color(0xFFF2FBFF),
//            gradientEndColor = Color(0xFFB3D4ED),
//            dropShadowColor = Color(0xFF00B7FF),
//            dropShadowBlurRadius = 1.4f,
//            dropShadowOffsetX = 0f,
//            dropShadowOffsetY = 0f,
//            strokeColor = Color(0xFFFCFCFC), // Figma stroke color (for reference)
//            strokeWeight = 0.2f, // Figma stroke weight (for reference)
//            // Example of changing font family and font weight
//            fontFamily = Montserrat, // Replace with your actual imported font family
//            fontWeight = FontWeight.Bold // Example font weight
//        )
//
//        Spacer(modifier = Modifier.width(8.dp))
//
//        // Right line
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 8.dp)
//                .height(1.dp)
//                .background(Color(0xFFFFFFFF))
//        )
//    }
//}
//
//
@Preview
@Composable
fun LineWithTextPreview() {
    LineWithText()
}
