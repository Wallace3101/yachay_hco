package com.william.yachay_hco.view.compose.profile.profile_screen.partes

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.model.User
import com.william.yachay_hco.ui.theme.*

@Composable
fun GlassmorphicProfileHeader(
    user: User?,
    displayName: String?,
    onEditClick: () -> Unit
) {
    val avatarInitial = when {
        !user?.firstName.isNullOrBlank() -> user?.firstName?.first()?.uppercase() ?: "E"
        !user?.username.isNullOrBlank() -> user?.username?.first()?.uppercase() ?: "E"
        else -> "E"
    }

    val displayUsername = user?.username ?: "explorador"
    var isHovered by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .scale(scale)
            .graphicsLayer {
                shadowElevation = 12f
                shape = RoundedCornerShape(28.dp)
                clip = true
            },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            BlueYachay.copy(alpha = 0.9f),
                            GreenYachay.copy(alpha = 0.8f),
                            YellowYachay.copy(alpha = 0.7f)
                        )
                    )
                )
                .drawBehind {
                    val dotSize = 2f
                    val spacing = 30f
                    for (x in 0 until (size.width / spacing).toInt()) {
                        for (y in 0 until (size.height / spacing).toInt()) {
                            drawCircle(
                                color = Color.White.copy(alpha = 0.1f),
                                radius = dotSize,
                                center = Offset(x * spacing, y * spacing)
                            )
                        }
                    }
                }
        ) {
            AnimatedBlob(modifier = Modifier.align(Alignment.TopEnd))

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Hola 👋",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    if (displayName != null) {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1
                        )
                    }

                    Text(
                        text = "@$displayUsername",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Badge de nivel
                    Surface(
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                        color = Color.White.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = YellowYachay,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text(
                                    text = "Nivel 3",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Explorador Avanzado",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Progreso",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "70%",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            AnimatedProgressBar(progress = 0.7f)
                        }
                    }
                }

                AvatarWithBadge(
                    initial = avatarInitial,
                    onEditClick = onEditClick
                )
            }
        }
    }
}

@Composable
fun AnimatedBlob(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "blob")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier
            .size(200.dp)
            .scale(scale)
            .graphicsLayer { alpha = 0.3f }
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                ),
                shape = CircleShape
            )
    )
}

@Composable
fun AnimatedProgressBar(progress: Float) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(animatedProgress)
            .fillMaxHeight()
            .clip(RoundedCornerShape(4.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        YellowYachay,
                        Color.White
                    )
                )
            )
    )
}

@Composable
fun AvatarWithBadge(
    initial: String,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(90.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.95f),
                            Color.White.copy(alpha = 0.8f)
                        )
                    )
                )
                .border(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White,
                            Color.White.copy(alpha = 0.6f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = BlueYachay
            )
        }
    }
}
