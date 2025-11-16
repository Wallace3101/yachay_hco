package com.william.yachay_hco.view.compose.profile.profile_screen.partes

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.YellowYachay

@Composable
fun AnimatedAchievementsGrid(totalAnalysis: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = YellowYachay,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Logros",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = BlueYachay
                )
            }

            val unlockedCount = getModernAchievements(totalAnalysis).count { it.unlocked }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = YellowYachay.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, YellowYachay.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "$unlockedCount/3",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = YellowYachay,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            getModernAchievements(totalAnalysis).forEach { achievement ->
                EnhancedAchievementCard(achievement)
            }
        }
    }
}

@Composable
fun EnhancedAchievementCard(achievement: ModernAchievement) {
    var isHovered by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 12.dp else 4.dp,
        label = "elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isHovered = !isHovered },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = BorderStroke(
            width = if (achievement.unlocked) 2.dp else 1.dp,
            color = if (achievement.unlocked)
                achievement.color.copy(alpha = 0.3f)
            else
                Color.Gray.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            achievement.color.copy(alpha = if (achievement.unlocked) 0.08f else 0.03f),
                            Color.Transparent
                        )
                    )
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icono con efecto glassmorphic
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = if (achievement.unlocked) listOf(
                                achievement.color,
                                achievement.color.copy(alpha = 0.7f)
                            ) else listOf(
                                Color.Gray.copy(alpha = 0.3f),
                                Color.Gray.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    achievement.icon,
                    contentDescription = null,
                    tint = if (achievement.unlocked) Color.White else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = achievement.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (achievement.unlocked) achievement.color else Color.Gray
                    )
                    if (achievement.unlocked) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Desbloqueado",
                            tint = GreenYachay,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Barra de progreso mejorada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.Gray.copy(alpha = 0.15f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(achievement.progress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        achievement.color,
                                        achievement.color.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                }

                Text(
                    text = "${(achievement.progress * 100).toInt()}% completado",
                    style = MaterialTheme.typography.labelSmall,
                    color = achievement.color,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}