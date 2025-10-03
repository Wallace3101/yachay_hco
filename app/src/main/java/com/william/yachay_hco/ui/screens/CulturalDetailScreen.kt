package com.william.yachay_hco.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.model.CulturalItem
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CulturalDetailScreen(
    culturalItem: CulturalItem,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Detalle Cultural") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            },
            actions = {
                IconButton(onClick = { /* Compartir */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Compartir")
                }
            }
        )

        if (!culturalItem.imagen.isNullOrEmpty()) {
            AsyncImage(
                model = culturalItem.imagen,
                contentDescription = culturalItem.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TÃ­tulo y categorÃ­a
            Column {
                Text(
                    text = culturalItem.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    ConfidenceIndicator(confidence = culturalItem.confianza)
                }
            }

            // DescripciÃ³n principal
            DetailCard(
                title = "DescripciÃ³n",
                content = culturalItem.descripcion,
                icon = Icons.Default.Description
            )

            // Contexto cultural
            DetailCard(
                title = "Contexto Cultural",
                content = culturalItem.contexto_cultural,
                icon = Icons.Default.Public
            )

            // InformaciÃ³n histÃ³rica
            DetailCard(
                title = "PerÃ­odo HistÃ³rico",
                content = culturalItem.periodo_historico,
                icon = Icons.Default.History
            )

            // UbicaciÃ³n
            DetailCard(
                title = "UbicaciÃ³n",
                content = culturalItem.ubicacion,
                icon = Icons.Default.LocationOn
            )

            // Significado
            DetailCard(
                title = "Significado Cultural",
                content = culturalItem.significado,
                icon = Icons.Default.Psychology
            )

            // InformaciÃ³n adicional
            AdditionalInfoCard(culturalItem = culturalItem)

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

fun Unit.isNotEmpty(): Boolean {
    return TODO("Provide the return value")
}

@Composable
fun DetailCard(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    if (content.isNotBlank()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )
            }
        }
    }
}

@Composable
fun AdditionalInfoCard(culturalItem: CulturalItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "InformaciÃ³n Adicional",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // Fecha de anÃ¡lisis
            culturalItem.createdAt?.let { createdAt ->
                InfoRow(
                    label = "Analizado el:",
                    value = formatDate(createdAt),
                    icon = Icons.Default.CalendarToday
                )
            }

            // Nivel de confianza detallado
            InfoRow(
                label = "Nivel de confianza:",
                value = "${(culturalItem.confianza * 100).toInt()}% - ${getConfidenceDescription(culturalItem.confianza)}",
                icon = Icons.Default.Analytics
            )


        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun getConfidenceDescription(confidence: Float): String {
    return when {
        confidence >= 0.9f -> "Muy alta"
        confidence >= 0.8f -> "Alta"
        confidence >= 0.7f -> "Buena"
        confidence >= 0.6f -> "Moderada"
        confidence >= 0.4f -> "Baja"
        else -> "Muy baja"
    }
}

private fun formatDate(dateString: String): String {
    return try {
        // Aqui puedes formatear la fecha segunn tu necesidad
        dateString.take(10) // Solo la fecha, sin la hora
    } catch (e: Exception) {
        dateString
    }
}

@Composable
fun ConfidenceIndicator(confidence: Float) {
    Surface(
        color = when {
            confidence >= 0.8f -> Color(0xFF4CAF50).copy(alpha = 0.2f)
            confidence >= 0.6f -> Color(0xFFFF9800).copy(alpha = 0.2f)
            else -> Color(0xFFF44336).copy(alpha = 0.2f)
        },
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "${(confidence * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = when {
                confidence >= 0.8f -> Color(0xFF4CAF50)
                confidence >= 0.6f -> Color(0xFFFF9800)
                else -> Color(0xFFF44336)
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}