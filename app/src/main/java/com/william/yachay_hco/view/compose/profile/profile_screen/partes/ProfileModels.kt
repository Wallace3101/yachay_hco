package com.william.yachay_hco.view.compose.profile.profile_screen.partes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.RedYachay
import com.william.yachay_hco.ui.theme.YellowYachay

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
        title = "Acerca de Yachay HCO",
        description = "Versión 1.0.0 • Powered by IA",
        icon = Icons.Default.Info,
        color = RedYachay,
        onClick = { }
    ),
    ModernSetting(
        title = "Cerrar Sesión",
        description = "Salir de tu cuenta actual",
        icon = Icons.Default.Logout,
        color = RedYachay,
        onClick = onLogout
    )
)
