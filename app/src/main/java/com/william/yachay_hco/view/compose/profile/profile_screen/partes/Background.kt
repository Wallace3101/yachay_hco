package com.william.yachay_hco.view.compose.profile.profile_screen.partes

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.william.yachay_hco.ui.theme.BlueYachay

@Composable
fun AnimatedBackgroundPattern() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offsetX"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val circles = listOf(
            Triple(size.width * 0.2f, size.height * 0.1f, 150f),
            Triple(size.width * 0.8f, size.height * 0.3f, 200f),
            Triple(size.width * 0.5f, size.height * 0.7f, 180f)
        )

        circles.forEach { (x, y, radius) ->
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        BlueYachay.copy(alpha = 0.03f),
                        Color.Transparent
                    ),
                    center = Offset(x + offsetX % size.width, y)
                ),
                radius = radius,
                center = Offset(x + offsetX % size.width, y)
            )
        }
    }
}
