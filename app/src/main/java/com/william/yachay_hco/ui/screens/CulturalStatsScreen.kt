package com.william.yachay_hco.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CulturalStatsScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas Culturales") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
            // Resumen general
            item {
                GeneralStatsCard()
            }

            // Análisis por categoría
            item {
                CategoryBreakdownCard()
            }

            // Progreso temporal
            item {
                TimeProgressCard()
            }

            // Nivel de confianza promedio
            item {
                ConfidenceStatsCard()
            }

            // Ubicaciones más analizadas
            item {
                LocationStatsCard()
            }

            // Logros y reconocimientos
            item {
                AchievementsCard()
            }
        }
    }
}

@Composable
fun GeneralStatsCard() {
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
            Text(
                text = "Resumen General",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCircle(
                    value = "24",
                    label = "Análisis\nRealizados",
                    color = GreenYachay,
                    progress = 0.6f
                )

                StatCircle(
                    value = "15",
                    label = "Elementos\nDescubiertos",
                    color = BlueYachay,
                    progress = 0.75f
                )

                StatCircle(
                    value = "87%",
                    label = "Precisión\nPromedio",
                    color = Color(0xFFFFB300),
                    progress = 0.87f
                )
            }
        }
    }
}

@Composable
fun StatCircle(
    value: String,
    label: String,
    color: Color,
    progress: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            // Círculo de fondo
            Canvas(modifier = Modifier.size(100.dp)) {
                // Fondo círculo
                drawCircle(
                    color = color.copy(alpha = 0.2f),
                    radius = size.minDimension / 2,
                    center = center
                )

                // Círculo de progreso
                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8.dp.toPx())
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun CategoryBreakdownCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Análisis por Categoría",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(16.dp))

            CategoryStatItem(
                category = CulturalCategory.GASTRONOMIA,
                count = 8,
                percentage = 0.4f,
                color = Color(0xFFFF7043)
            )

            CategoryStatItem(
                category = CulturalCategory.PATRIMONIO_ARQUEOLOGICO,
                count = 5,
                percentage = 0.25f,
                color = Color(0xFF8D6E63)
            )

            CategoryStatItem(
                category = CulturalCategory.FLORA_MEDICINAL,
                count = 6,
                percentage = 0.3f,
                color = Color(0xFF66BB6A)
            )

            CategoryStatItem(
                category = CulturalCategory.LEYENDAS_Y_TRADICIONES,
                count = 1,
                percentage = 0.05f,
                color = Color(0xFF42A5F5)
            )
        }
    }
}

@Composable
fun CategoryStatItem(
    category: CulturalCategory,
    count: Int,
    percentage: Float,
    color: Color
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {


                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "$count elementos",
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun TimeProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = GreenYachay.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Progreso en el Tiempo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = GreenYachay
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeStatItem("Esta semana", "5", Icons.Default.CalendarToday)
                TimeStatItem("Este mes", "18", Icons.Default.DateRange)
                TimeStatItem("Total", "24", Icons.Default.Timeline)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gráfico simple de barras
            SimpleBarChart(
                data = listOf(3, 7, 5, 9),
                labels = listOf("Sem 1", "Sem 2", "Sem 3", "Sem 4"),
                color = GreenYachay
            )
        }
    }
}

@Composable
fun TimeStatItem(
    label: String,
    value: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = GreenYachay,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = GreenYachay
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
    }
}

@Composable
fun SimpleBarChart(
    data: List<Int>,
    labels: List<String>,
    color: Color
) {
    val maxValue = data.maxOrNull() ?: 1

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEachIndexed { index, value ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height((value.toFloat() / maxValue * 60).dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(color)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = labels[index],
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ConfidenceStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Nivel de Confianza",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConfidenceLevelItem("Alta (>80%)", 12, Color(0xFF4CAF50))
                ConfidenceLevelItem("Media (60-80%)", 8, Color(0xFFFF9800))
                ConfidenceLevelItem("Baja (<60%)", 4, Color(0xFFF44336))
            }
        }
    }
}

@Composable
fun ConfidenceLevelItem(
    label: String,
    count: Int,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LocationStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ubicaciones Más Analizadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(16.dp))

            LocationItem("Centro de Huánuco", 8, 0.4f)
            LocationItem("Kotosh", 6, 0.3f)
            LocationItem("Pillco Marca", 4, 0.2f)
            LocationItem("Amarilis", 3, 0.15f)
            LocationItem("Otros", 3, 0.15f)
        }
    }
}

@Composable
fun LocationItem(
    location: String,
    count: Int,
    percentage: Float
) {
    Column(
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = location,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$count análisis",
                style = MaterialTheme.typography.labelMedium,
                color = BlueYachay
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = BlueYachay,
            trackColor = BlueYachay.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun AchievementsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFB300).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Logros Desbloqueados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFB300)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AchievementBadge(
                    icon = Icons.Default.CameraAlt,
                    title = "Primer\nAnálisis",
                    isUnlocked = true
                )

                AchievementBadge(
                    icon = Icons.Default.Explore,
                    title = "Explorador\nCultural",
                    isUnlocked = true
                )

                AchievementBadge(
                    icon = Icons.Default.Restaurant,
                    title = "Experto en\nGastronomía",
                    isUnlocked = true
                )

                AchievementBadge(
                    icon = Icons.Default.EmojiEvents,
                    title = "Maestro\nCultural",
                    isUnlocked = false
                )
            }
        }
    }
}

@Composable
fun AchievementBadge(
    icon: ImageVector,
    title: String,
    isUnlocked: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (isUnlocked) Color(0xFFFFB300) else Color.Gray.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isUnlocked) Color.White else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = if (isUnlocked) Color(0xFFFFB300) else Color.Gray
        )
    }
}