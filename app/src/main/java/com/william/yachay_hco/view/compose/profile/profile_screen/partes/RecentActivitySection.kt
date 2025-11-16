package com.william.yachay_hco.view.compose.profile.profile_screen.partes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.screens.getCategoryColor
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.RedYachay

@Composable
fun ModernRecentActivityCard(recentItems: List<CulturalItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.History,
                contentDescription = null,
                tint = BlueYachay,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "Actividad Reciente",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = BlueYachay
            )
        }

        if (recentItems.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(BlueYachay.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint = BlueYachay,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Text(
                        "Sin actividad reciente",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Comienza a explorar la cultura",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    recentItems.forEachIndexed { index, item ->
                        EnhancedActivityItem(
                            activity = ModernRecentActivity(
                                title = item.titulo,
                                description = "${item.categoria.displayName} • ${(item.confianza * 100).toInt()}% de precisión",
                                time = "Reciente",
                                icon = when (item.categoria) {
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
                                color = getCategoryColor(item.categoria),
                                highlight = index == 0
                            )
                        )
                        if (index < recentItems.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                color = Color.Gray.copy(alpha = 0.1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedActivityItem(activity: ModernRecentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            activity.color.copy(alpha = 0.2f),
                            activity.color.copy(alpha = 0.1f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = activity.color.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                activity.icon,
                contentDescription = null,
                tint = activity.color,
                modifier = Modifier.size(26.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = BlueYachay,
                maxLines = 1
            )
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (activity.highlight) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = RedYachay.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, RedYachay.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "Nuevo",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = RedYachay,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = activity.time,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}