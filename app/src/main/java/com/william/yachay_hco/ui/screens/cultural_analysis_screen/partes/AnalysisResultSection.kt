package com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.ui.screens.getCategoryColor
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.RedYachay
import com.william.yachay_hco.ui.theme.YellowYachay

@Composable
fun ModernAnalysisResultCard(
    culturalItem: com.william.yachay_hco.model.CulturalItem,
    onSave: () -> Unit,
    isSaving: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(28.dp)) {
            // Header con badge premium
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "✨ Resultado del Análisis",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = BlueYachay
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Identificación completada",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // Badge de confianza mejorado
                Surface(
                    color = getConfidenceColor(culturalItem.confianza).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = getConfidenceColor(culturalItem.confianza),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${(culturalItem.confianza * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = getConfidenceColor(culturalItem.confianza)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título destacado
            Text(
                text = culturalItem.titulo,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Categoría con icono
            Surface(
                color = getCategoryColor(culturalItem.categoria).copy(alpha = 0.15f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                        contentDescription = null,
                        tint = getCategoryColor(culturalItem.categoria),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = culturalItem.categoria.name.replace("_", " "),
                        style = MaterialTheme.typography.titleSmall,
                        color = getCategoryColor(culturalItem.categoria),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(24.dp))

            // Secciones de información mejoradas
            InfoSectionModern(
                icon = Icons.Default.Description,
                title = "Descripción",
                content = culturalItem.descripcion,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSectionModern(
                icon = Icons.Default.Info,
                title = "Contexto Cultural",
                content = culturalItem.contexto_cultural,
                color = GreenYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSectionModern(
                icon = Icons.Default.CalendarToday,
                title = "Periodo Histórico",
                content = culturalItem.periodo_historico,
                color = YellowYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSectionModern(
                icon = Icons.Default.LocationOn,
                title = "Ubicación",
                content = culturalItem.ubicacion,
                color = RedYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSectionModern(
                icon = Icons.Default.Star,
                title = "Significado",
                content = culturalItem.significado,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón guardar mejorado
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenYachay
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 4.dp
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Guardando...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Guardar en mi colección",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoSectionModern(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray,
            lineHeight = 24.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
    }
}