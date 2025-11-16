package com.william.yachay_hco.view.compose.profile.profile_screen.partes

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.RedYachay
import com.william.yachay_hco.ui.theme.YellowYachay
import com.william.yachay_hco.viewmodel.UserStats
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EnhancedStatsPager(
    userStats: UserStats,
    onStatsClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            pageSpacing = 16.dp
        ) { page ->
            val pageOffset = (
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    ).absoluteValue

            Card(
                onClick = onStatsClick,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                        scaleY = lerp(0.85f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                        scaleX = lerp(0.85f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (pageOffset < 0.5f) 8.dp else 4.dp
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = when (page) {
                        0 -> BlueYachay.copy(alpha = 0.1f)
                        else -> GreenYachay.copy(alpha = 0.1f)
                    }
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = when (page) {
                                    0 -> listOf(
                                        BlueYachay.copy(alpha = 0.03f),
                                        Color.Transparent
                                    )
                                    else -> listOf(
                                        GreenYachay.copy(alpha = 0.03f),
                                        Color.Transparent
                                    )
                                }
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (page) {
                        0 -> GlassmorphicStatsOverview(userStats)
                        1 -> EnhancedWeeklyProgress(userStats.weeklyAnalysis)
                    }
                }
            }
        }

        // Indicadores mejorados
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(2) { index ->
                val isSelected = index == pagerState.currentPage
                val color = if (isSelected) BlueYachay else Color.Gray.copy(alpha = 0.3f)
                val width by animateDpAsState(
                    targetValue = if (isSelected) 24.dp else 8.dp,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "width"
                )

                Box(
                    modifier = Modifier
                        .width(width)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color)
                )
                if (index < 1) Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun GlassmorphicStatsOverview(userStats: UserStats) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Estadísticas Generales",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = BlueYachay
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EnhancedStatItem(
                icon = Icons.Default.PhotoCamera,
                value = userStats.totalAnalysis.toString(),
                label = "Análisis",
                color = BlueYachay
            )
            EnhancedStatItem(
                icon = Icons.Default.Favorite,
                value = userStats.favorites.toString(),
                label = "Favoritos",
                color = RedYachay
            )
            EnhancedStatItem(
                icon = Icons.Default.Share,
                value = userStats.shared.toString(),
                label = "Compartidos",
                color = GreenYachay
            )
        }
    }
}

@Composable
fun EnhancedStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            color.copy(alpha = 0.15f),
                            color.copy(alpha = 0.05f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EnhancedWeeklyProgress(weeklyAnalysis: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            Icons.Default.TrendingUp,
            contentDescription = null,
            tint = GreenYachay,
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = "Progreso Semanal",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = BlueYachay
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = if (weeklyAnalysis > 0) "+$weeklyAnalysis" else "0",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = GreenYachay
            )
            Text(
                text = "análisis",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Gray.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((weeklyAnalysis / 10f).coerceAtMost(1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                GreenYachay,
                                YellowYachay
                            )
                        )
                    )
            )
        }

        Text(
            text = if (weeklyAnalysis >= 10) "🎉 ¡Meta alcanzada!" else "${10 - weeklyAnalysis} para alcanzar la meta",
            style = MaterialTheme.typography.bodySmall,
            color = if (weeklyAnalysis >= 10) GreenYachay else Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}