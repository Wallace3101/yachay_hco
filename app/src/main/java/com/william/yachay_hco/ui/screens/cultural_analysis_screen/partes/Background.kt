package com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.william.yachay_hco.R

@Composable
fun ModernCulturalBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // IMAGEN ÚNICA COMO FONDO COMPLETO
        Image(
            painter = painterResource(R.drawable.cultural_bg_2),
            contentDescription = "Fondo cultural completo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay sutil para mejorar legibilidad del contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.4f),
                            Color.White.copy(alpha = 0.2f)
                        )
                    )
                )
        )
    }
}
