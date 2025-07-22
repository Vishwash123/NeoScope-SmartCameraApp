package com.example.cameraapp.Components

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cameraapp.ChatbotBackend.SpeechManagerState
import kotlin.math.sin

@Composable
fun MicWaveWithRms(modifier: Modifier = Modifier,rmsDb: Float) {
    val rmsDb by SpeechManagerState.rmsDb.collectAsState()
    Toast.makeText(LocalContext.current,"mic rms: $rmsDb",Toast.LENGTH_SHORT).show()

    val clampedRms = rmsDb.coerceIn(0f, 12f)

    val targetAmplitude = clampedRms * 5f
    val targetSpeed = 1f + (clampedRms / 6f)

    val animatedAmplitude by animateFloatAsState(
        targetValue = targetAmplitude,
        animationSpec = tween(100)
    )

    val animatedSpeed by animateFloatAsState(
        targetValue = targetSpeed,
        animationSpec = tween(100)
    )

    WaterWave(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        waveColor = Color.Cyan,
        amplitude = animatedAmplitude,
        speed = animatedSpeed
    )
}




@Composable
fun WaterWave(
    modifier: Modifier = Modifier,
    waveColor: Color = Color(0xFF7BB1C0),
    amplitude: Float = 30f,
    waveCount: Int = 3,
    speed: Float = 1f
) {
    val infiniteTransition = rememberInfiniteTransition()
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((5000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        val circleRadius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        val clipPath = Path().apply {
            addOval(Rect(center = center, radius = circleRadius))
        }

        clipPath(clipPath) {
            val wavePath = Path()
            val waveLength = size.width / waveCount
            val centerY = size.height / 2f

            wavePath.moveTo(0f, centerY)

            for (x in 0..size.width.toInt()) {
                val angle = (2 * Math.PI * x / waveLength) + Math.toRadians(phase.toDouble())
                val y = centerY + amplitude * sin(angle).toFloat()
                wavePath.lineTo(x.toFloat(), y)
            }

            wavePath.lineTo(size.width, size.height)
            wavePath.lineTo(0f, size.height)
            wavePath.close()

            drawPath(path = wavePath, color = waveColor)
        }

        drawCircle(
            color = Color.Black,
            center = center,
            radius = circleRadius,
            style = Stroke(width = 4f)
        )
    }
}

