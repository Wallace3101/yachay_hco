package com.william.yachay_hco.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.CulturalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CulturalDetailScreen(
    itemId: Int,
    onNavigateBack: () -> Unit,
    viewModel: CulturalViewModel = hiltViewModel()
) {
    val culturalItem by viewModel.selectedCulturalItem.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(itemId) {
        viewModel.loadCulturalItemDetail(itemId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            ModernLoadingState()
        } else {
            culturalItem?.let { item ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    // Hero Image Section con gradiente
                    ModernHeroSection(item = item)

                    // Content Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Header con título y confianza
                        ModernHeaderSection(item = item)

                        // Descripción destacada
                        if (item.descripcion.isNotBlank()) {
                            ModernDescriptionCard(item.descripcion)
                        }

                        // Grid de información
                        ModernInfoGrid(item = item)

                        // Cards adicionales
                        if (item.contexto_cultural.isNotBlank()) {
                            ModernDetailCard(
                                title = "Contexto Cultural",
                                content = item.contexto_cultural,
                                icon = Icons.Default.Public,
                                accentColor = GreenYachay
                            )
                        }

                        if (item.periodo_historico.isNotBlank()) {
                            ModernDetailCard(
                                title = "Período Histórico",
                                content = item.periodo_historico,
                                icon = Icons.Default.History,
                                accentColor = YellowYachay
                            )
                        }

                        if (item.significado.isNotBlank()) {
                            ModernDetailCard(
                                title = "Significado Cultural",
                                content = item.significado,
                                icon = Icons.Default.Psychology,
                                accentColor = RedYachay
                            )
                        }

                        // Metadata footer
                        ModernMetadataCard(item = item)

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            } ?: EmptyState()
        }

        // Floating Back Button
        FloatingBackButton(
            onClick = onNavigateBack,
            scrollOffset = scrollState.value
        )
    }
}

@Composable
fun ModernHeroSection(item: CulturalItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        if (!item.imagen.isNullOrEmpty()) {
            AsyncImage(
                model = item.imagen,
                contentDescription = item.titulo,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradiente sobre la imagen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.background
                            ),
                            startY = 0f,
                            endY = 1000f
                        )
                    )
            )
        } else {
            // Placeholder con gradiente de colores Yachay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BlueYachay, GreenYachay)
                        )
                    )
            )
        }
    }
}

@Composable
fun ModernHeaderSection(item: CulturalItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.titulo,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            ModernConfidenceBadge(confidence = item.confianza)
        }
    }
}

@Composable
fun ModernConfidenceBadge(confidence: Float) {
    val (bgColor, textColor) = when {
        confidence >= 0.8f -> GreenYachay.copy(alpha = 0.15f) to GreenYachay
        confidence >= 0.6f -> YellowYachay.copy(alpha = 0.15f) to YellowYachay
        else -> RedYachay.copy(alpha = 0.15f) to RedYachay
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "${(confidence * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun ModernDescriptionCard(description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = BlueYachay.copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = BlueYachay.copy(alpha = 0.15f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = BlueYachay,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(20.dp)
                    )
                }
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueYachay
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )
        }
    }
}

@Composable
fun ModernInfoGrid(item: CulturalItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (item.ubicacion.isNotBlank()) {
            ModernInfoChip(
                icon = Icons.Default.LocationOn,
                label = "Ubicación",
                value = item.ubicacion,
                color = RedYachay
            )
        }
    }
}

@Composable
fun ModernInfoChip(
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.08f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.15f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(20.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ModernDetailCard(
    title: String,
    content: String,
    icon: ImageVector,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = accentColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Divider(
                color = accentColor.copy(alpha = 0.1f),
                thickness = 1.dp
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
fun ModernMetadataCard(item: CulturalItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CreamYachay.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Información del Análisis",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            item.created_at?.let { date ->
                MetadataRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Fecha de análisis",
                    value = formatDate(date)
                )
            }

            MetadataRow(
                icon = Icons.Default.Analytics,
                label = "Confiabilidad",
                value = getConfidenceDescription(item.confianza)
            )
        }
    }
}

@Composable
fun MetadataRow(icon: ImageVector, label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FloatingBackButton(onClick: () -> Unit, scrollOffset: Int) {
    val alpha by animateFloatAsState(
        targetValue = if (scrollOffset > 100) 0.95f else 1f,
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp)  // Cambié esta línea
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.TopStart),  // Y agregué esta línea
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = alpha),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ModernLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = BlueYachay,
                strokeWidth = 4.dp
            )
            Text(
                text = "Cargando información...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = RedYachay.copy(alpha = 0.1f),
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = null,
                    tint = RedYachay,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(40.dp)
                )
            }
            Text(
                text = "No encontrado",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "No se encontró información sobre este elemento cultural",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

private fun getConfidenceDescription(confidence: Float): String {
    return when {
        confidence >= 0.9f -> "Muy alta"
        confidence >= 0.8f -> "Alta"
        confidence >= 0.7f -> "Buena"
        confidence >= 0.6f -> "Moderada"
        confidence >= 0.4f -> "Baja"
        else -> "Muy baja"
    }
}

private fun formatDate(dateString: String): String {
    return try {
        dateString.take(10)
    } catch (e: Exception) {
        dateString
    }
}