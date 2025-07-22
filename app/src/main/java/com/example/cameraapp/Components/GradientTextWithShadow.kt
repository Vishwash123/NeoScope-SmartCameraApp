import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cameraapp.ui.theme.Montserrat

@OptIn(ExperimentalTextApi::class)
@Composable
public fun CustomizableGradientText(
    clickable:Boolean?=null,
    navHostController: NavHostController?=null,
    route:String?=null,
    name:String?=null,
    text: String,
    modifier: Modifier = Modifier,
    // Text properties
    fontSize: TextUnit = 14.sp,
    letterSpacing: TextUnit = 0.08.em, // 8% letter spacing
    fontFamily: FontFamily? = null, // Now an option to change font family
    fontWeight: FontWeight? = null, // Now an option to change font weight
    textAlign: TextAlign = TextAlign.Center,
    lineHeight: TextUnit = TextUnit.Unspecified,

    // Gradient properties
    gradientStartColor: Color = Color(0xFFF2FBFF),
    gradientEndColor: Color = Color(0xFFB3D4ED),

    // Drop Shadow properties
    dropShadowColor: Color = Color(0xFF00B7FF),
    dropShadowOffsetX: Float = 0f,
    dropShadowOffsetY: Float = 0f,
    dropShadowBlurRadius: Float = 1.4f,

    // Stroke Color (as noted before, direct text stroke in TextStyle is not available)
    strokeColor: Color = Color(0xFFFCFCFC),
    strokeWeight: Float = 0.2f // For reference if implementing custom drawing for stroke
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


@Preview()
@Composable
fun PreviewFigmaGradientText() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center) // Centers the content within the column
    ) {
        // Text with values directly from your Figma description
        CustomizableGradientText(
            text = "Your Figma Text",
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

        // Another example to demonstrate customization
//        CustomizableGradientText(
//            text = "Hello Gemini",
//            fontSize = 20.sp,
//            letterSpacing = 0.05.em,
//            gradientStartColor = Color.Magenta,
//            gradientEndColor = Color.Cyan,
//            dropShadowColor = Color.Black,
//            dropShadowBlurRadius = 4f,
//            modifier = Modifier.padding(top = 24.dp),
//            fontFamily = FontFamily.Cursive,
//            fontWeight = FontWeight.Light
//        )
    }
}