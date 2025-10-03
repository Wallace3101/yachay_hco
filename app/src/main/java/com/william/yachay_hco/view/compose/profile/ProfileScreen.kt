package com.william.yachay_hco.view.compose.profile

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.AuthViewModel
import com.william.yachay_hco.viewmodel.AuthResult
import com.william.yachay_hco.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToStats: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val currentUser by profileViewModel.userState.collectAsStateWithLifecycle()

    // Actualizar el usuario cuando se vuelve a la pantalla
    LaunchedEffect(Unit) {
        profileViewModel.refreshUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CreamYachay
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(CreamYachay, Color.White)
                    )
                ),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header del perfil
            item {
                ProfileHeader(
                    user = currentUser,
                    onEditClick = onNavigateToEdit
                )
            }

            // Estadísticas generales
            item {
                ProfileStats(onStatsClick = onNavigateToStats)
            }

            // Logros y badges
            item {
                AchievementsSection()
            }

            // Actividad reciente
            item {
                RecentActivitySection()
            }

            // Categorías favoritas
            item {
                FavoriteCategoriesSection()
            }

            // Configuración de cuenta
            item {
                AccountSettingsSection()
            }
        }
    }
}

@Composable
fun ProfileHeader(
    user: com.william.yachay_hco.model.User?,
    onEditClick: () -> Unit
) {
    // Obtener el nombre completo o username como fallback
    val displayName = when {
        !user?.firstName.isNullOrBlank() && !user?.lastName.isNullOrBlank() ->
            "${user?.firstName} ${user?.lastName}"
        !user?.firstName.isNullOrBlank() -> user?.firstName
        !user?.lastName.isNullOrBlank() -> user?.lastName
        else -> user?.username ?: "Usuario"
    }

    // Obtener la inicial para el avatar (priorizar firstName, luego username)
    val avatarInitial = when {
        !user?.firstName.isNullOrBlank() -> user?.firstName?.first()?.uppercase() ?: "U"
        !user?.username.isNullOrBlank() -> user?.username?.first()?.uppercase() ?: "U"
        else -> "U"
    }

    // Obtener username para mostrar
    val displayUsername = user?.username ?: "usuario"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BlueYachay.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(BlueYachay, BlueYachay.copy(alpha = 0.7f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatarInitial,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Botón de editar avatar
                FloatingActionButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp),
                    containerColor = GreenYachay,
                    contentColor = Color.White
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información del usuario - Nombre completo o username
            Text(
                text = displayName ?: "Usuario desconocido",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = BlueYachay
            )

            // Mostrar username si se está usando nombre completo
            if (!user?.firstName.isNullOrBlank() || !user?.lastName.isNullOrBlank()) {
                Text(
                    text = "@$displayUsername",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Email
            Text(
                text = user?.email ?: "usuario@email.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Insignia de nivel
            AssistChip(
                onClick = { },
                label = {
                    Text(
                        "Explorador Cultural",
                        fontWeight = FontWeight.Medium
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = GreenYachay.copy(alpha = 0.2f),
                    labelColor = GreenYachay,
                    leadingIconContentColor = GreenYachay
                )
            )
        }
    }
}

@Composable
fun ProfileStats(onStatsClick: () -> Unit) {
    Card(
        onClick = onStatsClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estadísticas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueYachay
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.PhotoCamera,
                    value = "24",
                    label = "Análisis",
                    color = GreenYachay
                )
                StatItem(
                    icon = Icons.Default.Favorite,
                    value = "15",
                    label = "Favoritos",
                    color = RedYachay
                )
                StatItem(
                    icon = Icons.Default.Share,
                    value = "8",
                    label = "Compartidos",
                    color = BlueYachay
                )
                StatItem(
                    icon = Icons.Default.EmojiEvents,
                    value = "3",
                    label = "Logros",
                    color = Color(0xFFFFB300)
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = BlueYachay
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Composable
fun AchievementsSection() {
    Column {
        Text(
            text = "Logros Desbloqueados",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BlueYachay
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(getMockAchievements()) { achievement ->
                AchievementCard(achievement = achievement)
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier.width(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = achievement.color.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, achievement.color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                achievement.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = achievement.color
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = achievement.title,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = achievement.color,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = achievement.description,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                maxLines = 2
            )
        }
    }
}

@Composable
fun RecentActivitySection() {
    Column {
        Text(
            text = "Actividad Reciente",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BlueYachay
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                getMockRecentActivity().forEach { activity ->
                    ActivityItem(activity = activity)
                }
            }
        }
    }
}

@Composable
fun ActivityItem(activity: RecentActivity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            activity.icon,
            contentDescription = null,
            tint = activity.color,
            modifier = Modifier.size(20.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = activity.description,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        Text(
            text = activity.time,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Composable
fun FavoriteCategoriesSection() {
    Column {
        Text(
            text = "Mis Categorías Favoritas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BlueYachay
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(CulturalCategory.values()) { category ->
                val isSelected = category == CulturalCategory.GASTRONOMIA ||
                        category == CulturalCategory.FLORA_MEDICINAL

                FilterChip(
                    onClick = { },
                    label = { Text(category.displayName) },
                    selected = isSelected,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GreenYachay.copy(alpha = 0.2f),
                        selectedLabelColor = GreenYachay
                    )
                )
            }
        }
    }
}

@Composable
fun AccountSettingsSection() {
    Column {
        Text(
            text = "Configuración de Cuenta",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BlueYachay
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "Notificaciones",
                    description = "Configurar alertas y recordatorios",
                    onClick = { }
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                SettingsItem(
                    icon = Icons.Default.Security,
                    title = "Privacidad y Seguridad",
                    description = "Gestionar datos y permisos",
                    onClick = { }
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                SettingsItem(
                    icon = Icons.Default.Help,
                    title = "Ayuda y Soporte",
                    description = "Preguntas frecuentes y contacto",
                    onClick = { }
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Acerca de YACHAY HCO",
                    description = "Versión 1.0.0",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = ripple(),
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = BlueYachay,
            modifier = Modifier.size(24.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

// Clases de datos para mock data
data class Achievement(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

data class RecentActivity(
    val title: String,
    val description: String,
    val time: String,
    val icon: ImageVector,
    val color: Color
)

// Mock data functions
fun getMockAchievements() = listOf(
    Achievement(
        title = "Primer Análisis",
        description = "Analizó su primera imagen",
        icon = Icons.Default.CameraAlt,
        color = GreenYachay
    ),
    Achievement(
        title = "Explorador",
        description = "10+ elementos analizados",
        icon = Icons.Default.Explore,
        color = BlueYachay
    ),
    Achievement(
        title = "Conocedor",
        description = "Experto en gastronomía",
        icon = Icons.Default.Restaurant,
        color = Color(0xFFFF7043)
    )
)

fun getMockRecentActivity() = listOf(
    RecentActivity(
        title = "Analizó Pachamanca",
        description = "Gastronomía • 95% confianza",
        time = "Hace 2h",
        icon = Icons.Default.Restaurant,
        color = Color(0xFFFF7043)
    ),
    RecentActivity(
        title = "Guardó en favoritos",
        description = "Templo de Kotosh",
        time = "Ayer",
        icon = Icons.Default.Favorite,
        color = RedYachay
    ),
    RecentActivity(
        title = "Compartió descubrimiento",
        description = "Muña Andina",
        time = "Hace 3 días",
        icon = Icons.Default.Share,
        color = BlueYachay
    )
)