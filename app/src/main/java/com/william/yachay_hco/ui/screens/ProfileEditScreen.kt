package com.william.yachay_hco.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.AuthResult
import com.william.yachay_hco.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAvatarSection(
    username: String,
    onEditAvatar: () -> Unit
) {
    Box(
        modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(BlueYachay, BlueYachay.copy(alpha = 0.7f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.firstOrNull()?.toString()?.uppercase() ?: "U",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Botón de editar
            FloatingActionButton(
                onClick = onEditAvatar,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp),
                containerColor = GreenYachay,
                contentColor = Color.White
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Cambiar avatar",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BlueYachay,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun InterestsSelector() {
    val interests = listOf(
        "Gastronomía" to Icons.Default.Restaurant,
        "Patrimonio Arqueológico" to Icons.Default.AccountBalance,
        "Flora Medicinal" to Icons.Default.Eco,
        "Leyendas y Tradiciones" to Icons.Default.AutoStories,
        "Arte Popular" to Icons.Default.Palette,
        "Música Folclórica" to Icons.Default.MusicNote
    )

    val selectedInterests = remember { mutableStateListOf<String>() }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        interests.chunked(2).forEach { rowInterests ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowInterests.forEach { (interest, icon) ->
                    val isSelected = selectedInterests.contains(interest)

                    FilterChip(
                        onClick = {
                            if (isSelected) {
                                selectedInterests.remove(interest)
                            } else {
                                selectedInterests.add(interest)
                            }
                        },
                        label = { Text(interest) },
                        selected = isSelected,
                        leadingIcon = {
                            Icon(
                                icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GreenYachay.copy(alpha = 0.2f),
                            selectedLabelColor = GreenYachay,
                            selectedLeadingIconColor = GreenYachay
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Si solo hay un elemento en la fila, agregar espacio
                if (rowInterests.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}