package com.william.yachay_hco.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.william.yachay_hco.R
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes.getConfidenceColor
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.CulturalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCultural: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToReports: () -> Unit,
    isAdmin: Boolean = false, // ← nuevo parámetro
    viewModel: CulturalViewModel = hiltViewModel(),
) {
    // AGREGAR ESTE LOG
    Log.d("MainScreen", "isAdmin value: $isAdmin")
    val culturalItems by viewModel.culturalItems.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadCulturalItems()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // MOSAICO CON LAS 4 IMÁGENES - CORREGIDO
        BackgroundMosaicPattern()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ModernFloatingHeader(onNavigateToProfile)
            },
            bottomBar = {
                ModernNavigationBar(
                    onNavigateToCultural = onNavigateToCultural,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToReports = onNavigateToReports,
                    isAdmin = isAdmin
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Hero Card con diseño premium
                HeroExplorationCard(onExploreClick = onNavigateToCultural)

                // Grid de estadísticas modernas
                ModernStatsGrid(
                    totalAnalysis = culturalItems.size,
                    discoveredItems = culturalItems.filter { it.confianza >= 0.8f }.size
                )

                // Items recientes con mejor diseño
                RecentCulturalItemsSection(
                    culturalItems = culturalItems.take(3),
                    isLoading = uiState.isLoading,
                    onViewDetails = onNavigateToDetail
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun BackgroundMosaicPattern() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamYachay)
    ) {
        // MOSAICO CON LAS 4 IMÁGENES QUE CUBRE TODA LA PANTALLA
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(R.drawable.img_cultural_1),
                    contentDescription = "Patrimonio cultural 1",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(R.drawable.img_cultural_2),
                    contentDescription = "Patrimonio cultural 2",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
            Row(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(R.drawable.img_cultural_3),
                    contentDescription = "Patrimonio cultural 3",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(R.drawable.img_cultural_4),
                    contentDescription = "Patrimonio cultural 4",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Overlay muy sutil para mejorar legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.4f),
                            Color.White.copy(alpha = 0.2f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun ModernFloatingHeader(onNavigateToProfile: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "Hola de nuevo 👋",
                style = MaterialTheme.typography.bodyMedium,
                color = BlueYachay,
                fontWeight = FontWeight.Medium
            )
            Text(
                "YACHAY HCO",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = BlueYachay,
                letterSpacing = 0.5.sp
            )
        }

        // Avatar con indicador
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(BlueYachay, GreenYachay)
                    )
                )
                .clickable { onNavigateToProfile() }
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun HeroExplorationCard(onExploreClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(28.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Explorador Cultural",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = BlueYachay
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Descubre el patrimonio de Huánuco con tecnología de análisis visual",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Icono flotante
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .shadow(8.dp, CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(GreenYachay, GreenYachay.copy(alpha = 0.8f))
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Cámara",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón CTA mejorado
                Button(
                    onClick = onExploreClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueYachay
                    ),
                    shape = RoundedCornerShape(18.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Text(
                        "Analizar Elemento Cultural",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Info pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoPill(
                        icon = Icons.Default.Speed,
                        text = "Rápido",
                        color = GreenYachay
                    )
                    InfoPill(
                        icon = Icons.Default.Psychology,
                        text = "IA Avanzada",
                        color = BlueYachay
                    )
                    InfoPill(
                        icon = Icons.Default.Security,
                        text = "Preciso",
                        color = YellowYachay
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ModernStatsGrid(
    totalAnalysis: Int,
    discoveredItems: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            value = totalAnalysis.toString(),
            label = "Análisis",
            icon = Icons.Default.Analytics,
            color = BlueYachay,
            modifier = Modifier.weight(1f)
        )

        StatCard(
            value = discoveredItems.toString(),
            label = "Descubiertos",
            icon = Icons.Default.Explore,
            color = GreenYachay,
            modifier = Modifier.weight(1f)
        )

        StatCard(
            value = "11",
            label = "Categorías",
            icon = Icons.Default.Category,
            color = YellowYachay,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )

            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RecentCulturalItemsSection(
    culturalItems: List<CulturalItem>,
    isLoading: Boolean,
    onViewDetails: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recientes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = BlueYachay
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                repeat(3) {
                    LoadingItemCard()
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            culturalItems.isEmpty() -> {
                EmptyStateCard()
            }
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    culturalItems.forEach { item ->
                        Log.d("RecentItems", "Item: ${item.titulo}, ID: ${item.id}")
                        ModernCulturalItemCard(
                            culturalItem = item,
                            onViewDetails = onViewDetails
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernCulturalItemCard(
    culturalItem: CulturalItem,
    onViewDetails: (Int) -> Unit
) {
    Card(
        onClick = {
            culturalItem.id?.let { id ->
                Log.d("CulturalItemCard", "Click en item ID: $id")
                onViewDetails(id)
            } ?: Log.e("CulturalItemCard", "Item sin ID")
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoría más grande y atractivo
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                getCategoryColor(culturalItem.categoria),
                                getCategoryColor(culturalItem.categoria).copy(alpha = 0.8f)
                            )
                        ),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (culturalItem.categoria) {
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
                    contentDescription = culturalItem.categoria.name,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    culturalItem.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BlueYachay,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    culturalItem.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge de categoría
                    Surface(
                        color = getCategoryColor(culturalItem.categoria).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            culturalItem.categoria.name.replace("_", " ").take(12),
                            style = MaterialTheme.typography.labelSmall,
                            color = getCategoryColor(culturalItem.categoria),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Badge de confianza
                    Surface(
                        color = getConfidenceColor(culturalItem.confianza).copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = null,
                                tint = getConfidenceColor(culturalItem.confianza),
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                "${(culturalItem.confianza * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = getConfidenceColor(culturalItem.confianza),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ver detalles",
                tint = BlueYachay,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun LoadingItemCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = BlueYachay,
                strokeWidth = 3.dp
            )
        }
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(GreenYachay.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Explore,
                    contentDescription = null,
                    tint = GreenYachay,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "¡Comienza tu exploración!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Analiza tu primera imagen cultural y descubre el patrimonio de Huánuco",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ModernNavigationBar(
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
                    onClick = onNavigateToReports  // Navega a admin_reports
                )
            } else {
                NavBarItem(
                    icon = Icons.Default.Report,
                    label = "Reportes",
                    isSelected = false,
                    onClick = onNavigateToReports  // Navega a reports
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
    onClick: () -> Unit
) {
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
            tint = if (isSelected) BlueYachay else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) BlueYachay else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenModernPreview() {
    MaterialTheme {
        MainScreen({}, {}, {}, {})
    }
}