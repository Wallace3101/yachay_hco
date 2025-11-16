package com.william.yachay_hco.ui.screens.cultural_detail_screen.partes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.RedYachay

@Composable
fun FloatingBackButton(onClick: () -> Unit, scrollOffset: Int) {
    val alpha by animateFloatAsState(
        targetValue = if (scrollOffset > 100) 0.95f else 1f,
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp)  // Cambié esta línea
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.TopStart),  // Y agregué esta línea
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = alpha),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ModernLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = BlueYachay,
                strokeWidth = 4.dp
            )
            Text(
                text = "Cargando información...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = RedYachay.copy(alpha = 0.1f),
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = null,
                    tint = RedYachay,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(40.dp)
                )
            }
            Text(
                text = "No encontrado",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "No se encontró información sobre este elemento cultural",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}