package com.william.yachay_hco.view.compose.profile

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.model.User
import com.william.yachay_hco.ui.screens.getCategoryColor
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.ProfileViewModel
import com.william.yachay_hco.viewmodel.UserStats
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToStats: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val currentUser by profileViewModel.userState.collectAsStateWithLifecycle()
    val userStats by profileViewModel.userStats.collectAsStateWithLifecycle()
    val culturalItems by profileViewModel.culturalItems.collectAsStateWithLifecycle()

    val displayName = when {
        !currentUser?.firstName.isNullOrBlank() && !currentUser?.lastName.isNullOrBlank() ->
            "${currentUser?.firstName} ${currentUser?.lastName}"
        !currentUser?.firstName.isNullOrBlank() -> currentUser?.firstName
        !currentUser?.lastName.isNullOrBlank() -> currentUser?.lastName
        else -> currentUser?.username ?: "Explorador Cultural"
    }

    LaunchedEffect(Unit) {
        profileViewModel.refreshUser()
    }

    // 👇 AGREGAR: Log para debugging
    LaunchedEffect(userStats) {
        Log.d("ProfileScreen", "Stats updated - Total: ${userStats.totalAnalysis}, Weekly: ${userStats.weeklyAnalysis}")
    }

    LaunchedEffect(culturalItems) {
        Log.d("ProfileScreen", "Cultural items loaded: ${culturalItems.size}")
        culturalItems.forEach { item ->
            Log.d("ProfileScreen", "Item: ${item.titulo}, Created by: ${item.created_by_id}, Date: ${item.created_at}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BlueYachay.copy(alpha = 0.05f),
                        GreenYachay.copy(alpha = 0.02f),
                        Color.White
                    )
                )
            )
    ) {
        // Patrón de fondo decorativo animado
        AnimatedBackgroundPattern()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ModernTopBar(
                    onNavigateBack = onNavigateBack,
                    onNavigateToEdit = onNavigateToEdit
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    GlassmorphicProfileHeader(
                        user = currentUser,
                        displayName = displayName,
                        onEditClick = onNavigateToEdit
                    )
                }

                item {
                    EnhancedStatsPager(
                        userStats = userStats,
                        onStatsClick = onNavigateToStats
                    )
                }

                item {
                    AnimatedAchievementsGrid(
                        totalAnalysis = userStats.totalAnalysis
                    )
                }

                item {
                    ModernRecentActivityCard(
                        recentItems = culturalItems.take(3)
                    )
                }

                item {
                    InteractiveCategoriesSection(
                        culturalItems = culturalItems
                    )
                }

                item {
                    GlassmorphicSettingsCard(onLogout = onLogout)
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun AnimatedBackgroundPattern() {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offsetX"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val circles = listOf(
            Triple(size.width * 0.2f, size.height * 0.1f, 150f),
            Triple(size.width * 0.8f, size.height * 0.3f, 200f),
            Triple(size.width * 0.5f, size.height * 0.7f, 180f)
        )

        circles.forEach { (x, y, radius) ->
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        BlueYachay.copy(alpha = 0.03f),
                        Color.Transparent
                    ),
                    center = Offset(x + offsetX % size.width, y)
                ),
                radius = radius,
                center = Offset(x + offsetX % size.width, y)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(GreenYachay)
                )
                Text(
                    "Mi Perfil",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = BlueYachay
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.9f),
                                Color.White.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = BlueYachay.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = BlueYachay,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

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
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .drawBehind {
                    // Patrón de puntos decorativo
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
            // Blob decorativo animado
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

                    // Badge de nivel mejorado
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

                    // Barra de progreso mejorada
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

                // Avatar glassmorphic mejorado
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
        // Avatar principal con glassmorphism
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

@Composable
fun GlassmorphicSettingsCard(onLogout: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = null,
                tint = BlueYachay,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = "Configuración",
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
            Column {
                getModernSettings(onLogout).forEachIndexed { index, setting ->
                    EnhancedSettingsItem(setting)
                    if (index < getModernSettings(onLogout).size - 1) {
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

@Composable
fun EnhancedSettingsItem(setting: ModernSetting) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { setting.onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            setting.color.copy(alpha = 0.15f),
                            setting.color.copy(alpha = 0.08f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = setting.color.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                setting.icon,
                contentDescription = null,
                tint = setting.color,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = setting.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = BlueYachay
            )
            Text(
                text = setting.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray.copy(alpha = 0.8f)
            )
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray.copy(alpha = 0.4f),
            modifier = Modifier.size(24.dp)
        )
    }
}

// Data classes
data class ModernAchievement(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color,
    val progress: Float,
    val unlocked: Boolean
)

data class ModernRecentActivity(
    val title: String,
    val description: String,
    val time: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color,
    val highlight: Boolean = false
)

data class ModernSetting(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

// Helper functions
fun getModernAchievements(totalAnalysis: Int) = listOf(
    ModernAchievement(
        title = "Pionero Cultural",
        description = "Analiza 10 elementos diferentes",
        icon = Icons.Default.Explore,
        color = BlueYachay,
        progress = (totalAnalysis / 10f).coerceAtMost(1f),
        unlocked = totalAnalysis >= 10
    ),
    ModernAchievement(
        title = "Explorador Activo",
        description = "Realiza 5 análisis",
        icon = Icons.Default.PhotoCamera,
        color = GreenYachay,
        progress = (totalAnalysis / 5f).coerceAtMost(1f),
        unlocked = totalAnalysis >= 5
    ),
    ModernAchievement(
        title = "Primer Paso",
        description = "Completa tu primer análisis",
        icon = Icons.Default.Star,
        color = YellowYachay,
        progress = if (totalAnalysis > 0) 1f else 0f,
        unlocked = totalAnalysis > 0
    )
)

fun getModernSettings(onLogout: () -> Unit) = listOf(
    ModernSetting(
        title = "Notificaciones",
        description = "Alertas y recordatorios personalizados",
        icon = Icons.Default.Notifications,
        color = BlueYachay,
        onClick = { }
    ),
    ModernSetting(
        title = "Privacidad y Seguridad",
        description = "Controla tu información y datos",
        icon = Icons.Default.Security,
        color = GreenYachay,
        onClick = { }
    ),
    ModernSetting(
        title = "Ayuda y Soporte",
        description = "Centro de ayuda y contacto",
        icon = Icons.Default.Help,
        color = YellowYachay,
        onClick = { }
    ),
    ModernSetting(
        title = "Acerca de Yachay HCO",
        description = "Versión 2.0.0 • Powered by IA",
        icon = Icons.Default.Info,
        color = RedYachay,
        onClick = { }
    ),
    ModernSetting(
        title = "Cerrar Sesión",
        description = "Salir de tu cuenta actual",
        icon = Icons.Default.Logout,  // Asegúrate de importar: import androidx.compose.material.icons.filled.Logout
        color = RedYachay,
        onClick = onLogout
    )
)