package com.william.yachay_hco.ui.screens.cultural_detail_screen.partes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay

@Composable
fun ModernHeroSection(item: CulturalItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        if (!item.imagen.isNullOrEmpty()) {
            AsyncImage(
                model = item.imagen,
                contentDescription = item.titulo,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradiente sobre la imagen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.background
                            ),
                            startY = 0f,
                            endY = 1000f
                        )
                    )
            )
        } else {
            // Placeholder con gradiente de colores Yachay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BlueYachay, GreenYachay)
                        )
                    )
            )
        }
    }
}