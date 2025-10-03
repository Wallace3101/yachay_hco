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
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.screens.getCategoryColor
import com.william.yachay_hco.ui.theme.*

@Composable
fun AnalysisResultCard(
    culturalItem: CulturalItem,
    onSave: () -> Unit,
    isSaving: Boolean
) {
    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Encabezado con confianza
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Resultado del Análisis",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BlueYachay
                )

                // Badge de confianza
                Surface(
                    color = getConfidenceColor(culturalItem.confianza).copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${(culturalItem.confianza * 100).toInt()}% confianza",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = getConfidenceColor(culturalItem.confianza),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = culturalItem.titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Categoría
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = getCategoryColor(culturalItem.categoria).copy(alpha = 0.1f),
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = getCategoryColor(culturalItem.categoria),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = culturalItem.categoria.name.replace("_", " "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = getCategoryColor(culturalItem.categoria),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            // Secciones de información
            InfoSection(
                icon = Icons.Default.Description,
                title = "Descripción",
                content = culturalItem.descripcion
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(
                icon = Icons.Default.Info,
                title = "Contexto Cultural",
                content = culturalItem.contexto_cultural
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(
                icon = Icons.Default.CalendarToday,
                title = "Periodo Histórico",
                content = culturalItem.periodo_historico
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(
                icon = Icons.Default.LocationOn,
                title = "Ubicación",
                content = culturalItem.ubicacion
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoSection(
                icon = Icons.Default.Star,
                title = "Significado",
                content = culturalItem.significado
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de guardar
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenYachay,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Guardando...",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Guardar en Mi Colección",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}