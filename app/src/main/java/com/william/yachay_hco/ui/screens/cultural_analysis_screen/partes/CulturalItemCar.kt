package com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.screens.getCategoryColor
import com.william.yachay_hco.ui.theme.*

@Composable
fun CulturalItemCard(culturalItem: CulturalItem) {
    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado con categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = culturalItem.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                // Badge de categoría
                Surface(
                    color = getCategoryColor(culturalItem.categoria).copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = culturalItem.categoria.name.replace("_", " "),
                        style = MaterialTheme.typography.labelSmall,
                        color = getCategoryColor(culturalItem.categoria),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción breve
            Text(
                text = culturalItem.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Ubicación
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GreenYachay,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = culturalItem.ubicacion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Confianza
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = getConfidenceColor(culturalItem.confianza).copy(alpha = 0.1f),
                        shape = CircleShape,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = getConfidenceColor(culturalItem.confianza),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${(culturalItem.confianza * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = getConfidenceColor(culturalItem.confianza)
                    )
                }
            }
        }
    }
}

fun getConfidenceColor(confidence: Float): Color {
    return when {
        confidence >= 0.8f -> GreenYachay
        confidence >= 0.5f -> YellowYachay
        else -> RedYachay
    }
}