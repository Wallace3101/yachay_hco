package com.william.yachay_hco.ui.screens.main_screen.partes

import android.util.Log
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes.getConfidenceColor
import com.william.yachay_hco.ui.screens.getCategoryColor
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import kotlin.collections.forEach

@Composable
fun RecentCulturalItemsSection(
    culturalItems: List<CulturalItem>,
    isLoading: Boolean,
    onViewDetails: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recientes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = BlueYachay
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                repeat(3) {
                    LoadingItemCard()
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            culturalItems.isEmpty() -> {
                EmptyStateCard()
            }
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    culturalItems.forEach { item ->
                        Log.d("RecentItems", "Item: ${item.titulo}, ID: ${item.id}")
                        ModernCulturalItemCard(
                            culturalItem = item,
                            onViewDetails = onViewDetails
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernCulturalItemCard(
    culturalItem: CulturalItem,
    onViewDetails: (Int) -> Unit
) {
    Card(
        onClick = {
            culturalItem.id?.let { id ->
                Log.d("CulturalItemCard", "Click en item ID: $id")
                onViewDetails(id)
            } ?: Log.e("CulturalItemCard", "Item sin ID")
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoría más grande y atractivo
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                getCategoryColor(culturalItem.categoria),
                                getCategoryColor(culturalItem.categoria).copy(alpha = 0.8f)
                            )
                        ),
                        RoundedCornerShape(16.dp)
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
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    culturalItem.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BlueYachay,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    culturalItem.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge de categoría
                    Surface(
                        color = getCategoryColor(culturalItem.categoria).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            culturalItem.categoria.name.replace("_", " ").take(12),
                            style = MaterialTheme.typography.labelSmall,
                            color = getCategoryColor(culturalItem.categoria),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Badge de confianza
                    Surface(
                        color = getConfidenceColor(culturalItem.confianza).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = null,
                                tint = getConfidenceColor(culturalItem.confianza),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                "${(culturalItem.confianza * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = getConfidenceColor(culturalItem.confianza),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ver detalles",
                tint = BlueYachay,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun LoadingItemCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = BlueYachay,
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(GreenYachay.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Explore,
                    contentDescription = null,
                    tint = GreenYachay,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "¡Comienza tu exploración!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Analiza tu primera imagen cultural y descubre el patrimonio de Huánuco",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}