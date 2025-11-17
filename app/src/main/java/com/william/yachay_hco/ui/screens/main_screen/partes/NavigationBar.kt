package com.william.yachay_hco.ui.screens.main_screen.partes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.RedYachay

@Composable
fun ModernNavigationBar(
    onNavigateToCultural: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToReports: () -> Unit,
    isAdmin: Boolean = false, // ← nuevo parámetro
) {
    // AGREGAR ESTE LOG
    Log.d("MainScreen", "isAdmin value: $isAdmin")
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                isSelected = true,
                onClick = { }
            )

            // CAMBIAR ESTO: Solo un botón que cambia según el rol
            if (isAdmin) {
                NavBarItem(
                    icon = Icons.Default.AdminPanelSettings,
                    label = "Admin",
                    isSelected = false,
                    onClick = onNavigateToReports,  // Navega a admin_reports
                    tintColor = RedYachay,
                )
            } else {
                NavBarItem(
                    icon = Icons.Default.Report,
                    label = "Reportes",
                    isSelected = false,
                    onClick = onNavigateToReports,  // Navega a reports
                    tintColor = RedYachay,
                )
            }

            // Botón central especial
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(8.dp, CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BlueYachay, GreenYachay)
                        ),
                        CircleShape
                    )
                    .clickable { onNavigateToCultural() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Analizar",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            NavBarItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                isSelected = false,
                onClick = onNavigateToProfile
            )
        }
    }
}

@Composable
private fun NavBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    tintColor: Color? = null
) {
    val iconColor = tintColor ?: (if (isSelected) BlueYachay else Color.Gray)
    val textColor = tintColor ?: (if (isSelected) BlueYachay else Color.Gray)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = if (isSelected || tintColor != null) FontWeight.Bold else FontWeight.Normal
        )
    }
}