package com.william.yachay_hco.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.william.yachay_hco.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCultural: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            // Header moderno estilo imagen
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BlueYachay),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Bienvenido a",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            "YACHAY HCO",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }

                    // Avatar/Logo circular
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "Logo Cultural",
                            tint = BlueYachay,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 12.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .shadow(8.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                listOf("Inicio", "Cultural", "Perfil").forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = index == 0,
                        onClick = {
                            when (index) {
                                1 -> onNavigateToCultural()
                                2 -> onNavigateToProfile()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.Home
                                    1 -> Icons.Default.Explore
                                    else -> Icons.Default.Person
                                },
                                contentDescription = label,
                                tint = if (index == 0) BlueYachay else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                label,
                                color = if (index == 0) BlueYachay else Color.Gray,
                                fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BlueYachay,
                            selectedTextColor = BlueYachay,
                            indicatorColor = BlueYachay.copy(0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CreamYachay.copy(alpha = 0.2f),
                            Color.White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Sección de Exploración Cultural (estilo imagen)
                CulturalExplorationSection(
                    onExploreClick = onNavigateToCultural
                )

                // Sección de Elementos Culturales Populares
                PopularCulturalItemsSection()

                // Sección de Estadísticas Rápidas
                QuickStatsSection()

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CulturalExplorationSection(
    onExploreClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Explorador Cultural",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = BlueYachay
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "Descubre la riqueza cultural de Huánuco",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // Icono de cultura (reemplazo del planeta)
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(GreenYachay, GreenYachay.copy(alpha = 0.7f))
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "Patrimonio Cultural",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de exploración (flecha de la imagen)
            Button(
                onClick = onExploreClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenYachay,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Analizar Imagen Cultural",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Analizar imagen",
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Usa tu cámara para explorar artefactos culturales",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )

                // Indicador de actividad (similar a "8 friends online")
                Surface(
                    color = GreenYachay.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Listo para analizar",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreenYachay,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PopularCulturalItemsSection() {
    Column {
        Text(
            "Elementos Populares",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Lista de elementos culturales populares (estilo imagen)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PopularCulturalItem(
                title = "Cerámica de Huarguish y Punchao Chico",
                description = "Artesanía alfarera tradicional de Huánuco, con ollas y recipientes utilitarios elaborados con arcillas locales (raku, ushia, muki).",
                category = "Patrimonio Artesanal / Cerámica",
                confidence = 0.93f
            )

            PopularCulturalItem(
                title = "Pachamanca Huanuqueña",
                description = "Técnica ancestral de cocción bajo tierra con piedras calientes; incluye carnes, verduras, tubérculos propios de la zona.",
                category = "Gastronomía",
                confidence = 0.90f
            )

            PopularCulturalItem(
                title = "Textiles Huanuqueños",
                description = "Tejidos tradicionales realizados con lana de alpaca u oveja, patrones locales y diseños que reflejan la conexión con la naturaleza y simbología andina.",
                category = "Arte Textil",
                confidence = 0.88f
            )
        }
    }
}

@Composable
private fun PopularCulturalItem(
    title: String,
    description: String,
    category: String,
    confidence: Float
) {
    Card(
        onClick = { /* Navegar a detalle */ },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono representativo
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        when (category) {
                            "Patrimonio Arqueológico" -> Color(0xFF8D6E63).copy(alpha = 0.1f)
                            "Gastronomía" -> Color(0xFFFF7043).copy(alpha = 0.1f)
                            else -> BlueYachay.copy(alpha = 0.1f)
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (category) {
                        "Patrimonio Arqueológico" -> Icons.Default.AccountBalance
                        "Gastronomía" -> Icons.Default.Restaurant
                        else -> Icons.Default.Palette
                    },
                    contentDescription = category,
                    tint = when (category) {
                        "Patrimonio Arqueológico" -> Color(0xFF8D6E63)
                        "Gastronomía" -> Color(0xFFFF7043)
                        else -> BlueYachay
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    description,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Badge de categoría y confianza
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = BlueYachay.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            category,
                            style = MaterialTheme.typography.labelSmall,
                            color = BlueYachay,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        color = when {
                            confidence >= 0.9f -> GreenYachay.copy(alpha = 0.1f)
                            confidence >= 0.8f -> YellowYachay.copy(alpha = 0.1f)
                            else -> RedYachay.copy(alpha = 0.1f)
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "${(confidence * 100).toInt()}% confianza",
                            style = MaterialTheme.typography.labelSmall,
                            color = when {
                                confidence >= 0.9f -> GreenYachay
                                confidence >= 0.8f -> YellowYachay
                                else -> RedYachay
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickStatsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BlueYachay),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Tu Progreso Cultural",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickStatItem(
                    value = "12",
                    label = "Análisis",
                    icon = Icons.Default.CameraAlt
                )

                QuickStatItem(
                    value = "8",
                    label = "Descubierto",
                    icon = Icons.Default.Explore
                )

                QuickStatItem(
                    value = "4",
                    label = "Categorías",
                    icon = Icons.Default.Category
                )
            }
        }
    }
}

@Composable
private fun QuickStatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenModernPreview() {
    MaterialTheme {
        MainScreen({}, {}, {})
    }
}