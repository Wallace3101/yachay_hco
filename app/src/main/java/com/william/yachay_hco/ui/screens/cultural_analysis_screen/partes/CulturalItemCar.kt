package com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.ui.screens.getCategoryColor
import com.william.yachay_hco.ui.theme.*

@Composable
fun ModernCulturalItemCard(
    culturalItem: com.william.yachay_hco.model.CulturalItem,
    onViewDetails: (Int) -> Unit,
) {
    Card(
        onClick = {
            culturalItem.id?.let { id ->
                onViewDetails(id)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoría destacado
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .shadow(6.dp, RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                getCategoryColor(culturalItem.categoria),
                                getCategoryColor(culturalItem.categoria).copy(alpha = 0.7f)
                            )
                        ),
                        RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (culturalItem.categoria) {
                        CulturalCategory.GASTRONOMIA -> Icons.Default.Restaurant
                        CulturalCategory.PATRIMONIO_ARQUEOLOGICO -> Icons.Default.AccountBalance
                        CulturalCategory.FLORA_MEDICINAL -> Icons.Default.LocalFlorist
                        CulturalCategory.LEYENDAS_Y_TRADICIONES -> Icons.Default.AutoStories
                        CulturalCategory.FESTIVIDADES -> Icons.Default.Celebration
                        CulturalCategory.DANZA -> Icons.Default.DirectionsRun
                        CulturalCategory.MUSICA -> Icons.Default.MusicNote
                        CulturalCategory.VESTIMENTA -> Icons.Default.Checkroom
                        CulturalCategory.ARTE_POPULAR -> Icons.Default.Palette
                        CulturalCategory.NATURALEZA_CULTURAL -> Icons.Default.Public
                        CulturalCategory.OTRO -> Icons.Default.Category

                    },
                    contentDescription = culturalItem.categoria.name,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp) // Espacio para la flecha
            ) {
                Text(
                    text = culturalItem.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BlueYachay,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = culturalItem.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge de categoría
                    Surface(
                        color = getCategoryColor(culturalItem.categoria).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = culturalItem.categoria.name.replace("_", " ").take(15),
                            style = MaterialTheme.typography.labelSmall,
                            color = getCategoryColor(culturalItem.categoria),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    // Badge de confianza
                    Surface(
                        color = getConfidenceColor(culturalItem.confianza).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = getConfidenceColor(culturalItem.confianza),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                "${(culturalItem.confianza * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = getConfidenceColor(culturalItem.confianza)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ubicación
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = GreenYachay,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = culturalItem.ubicacion,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Botón de acción más prominente
            FloatingActionButton(
                onClick = {
                    culturalItem.id?.let { id ->
                        onViewDetails(id)
                    }
                },
                modifier = Modifier.size(36.dp),
                containerColor = BlueYachay,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Ver detalles",
                    modifier = Modifier.size(18.dp)
                )
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