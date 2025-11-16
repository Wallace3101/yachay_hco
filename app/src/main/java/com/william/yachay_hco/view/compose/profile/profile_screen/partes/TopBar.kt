package com.william.yachay_hco.view.compose.profile.profile_screen.partes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(
    onNavigateBack: () -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) } // por ahora sin uso, podrías eliminarlo

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
