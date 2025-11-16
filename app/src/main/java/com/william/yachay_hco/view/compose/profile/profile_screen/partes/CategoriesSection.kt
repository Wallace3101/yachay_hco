package com.william.yachay_hco.view.compose.profile.profile_screen.partes

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun InteractiveCategoriesSection(culturalItems: List<CulturalItem>) {
    val categoryCounts = culturalItems.groupingBy { it.categoria }.eachCount()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Category,
                contentDescription = null,
                tint = BlueYachay,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "Mis Categorías",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = BlueYachay
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CulturalCategory.values().forEach { category ->
                    val count = categoryCounts[category] ?: 0
                    CategoryProgressItem(
                        category = category,
                        count = count,
                        isActive = count > 0
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryProgressItem(
    category: CulturalCategory,
    count: Int,
    isActive: Boolean
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (isActive) (count / 10f).coerceAtMost(1f) else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isActive)
                                getCategoryColor(category).copy(alpha = 0.15f)
                            else
                                Color.Gray.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        when (category) {
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
                        tint = if (isActive) getCategoryColor(category) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                    color = if (isActive) BlueYachay else Color.Gray
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isActive)
                    getCategoryColor(category).copy(alpha = 0.15f)
                else
                    Color.Gray.copy(alpha = 0.1f)
            ) {
                Text(
                    text = count.toString(),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isActive) getCategoryColor(category) else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.Gray.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                getCategoryColor(category),
                                getCategoryColor(category).copy(alpha = 0.7f)
                            )
                        )
                    )
            )
        }
    }
}