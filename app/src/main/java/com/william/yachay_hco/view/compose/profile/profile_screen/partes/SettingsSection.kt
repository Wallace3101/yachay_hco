package com.william.yachay_hco.view.compose.profile.profile_screen.partes

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.BlueYachay

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