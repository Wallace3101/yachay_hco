package com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.*

// Data class para representar las categorías
data class CulturalCategory(
    val id: String,
    val displayName: String,
    val color: Color,
    val textColor: Color = Color.White
)

// Definición de las categorías usando la paleta Yachay
val culturalCategories = listOf(
    CulturalCategory(
        id = "GASTRONOMIA",
        displayName = "Gastronomía",
        color = RedYachay
    ),
    CulturalCategory(
        id = "PATRIMONIO_ARQUEOLOGICO",
        displayName = "Patrimonio Arqueológico",
        color = BlueYachay
    ),
    CulturalCategory(
        id = "FLORA_MEDICINAL",
        displayName = "Flora Medicinal",
        color = GreenYachay
    ),
    CulturalCategory(
        id = "LEYENDAS_Y_TRADICIONES",
        displayName = "Leyendas y Tradiciones",
        color = YellowYachay,
        textColor = Color.Black // Texto negro para mejor contraste con amarillo
    )
)

@Composable
fun CategoryFilterCard(
    selectedCategories: Set<String> = emptySet(),
    onCategorySelected: (String) -> Unit = {},
    onCategoryDeselected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header con icono
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtrar por categoría",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueYachay
                )

                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtrar",
                    tint = BlueYachay,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contador de filtros activos
            if (selectedCategories.isNotEmpty()) {
                Surface(
                    color = BlueYachay.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${selectedCategories.size} categoría(s) seleccionada(s)",
                        style = MaterialTheme.typography.labelSmall,
                        color = BlueYachay,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Lista horizontal de categorías
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(culturalCategories) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = selectedCategories.contains(category.id),
                        onSelectionChanged = { selected ->
                            if (selected) {
                                onCategorySelected(category.id)
                            } else {
                                onCategoryDeselected(category.id)
                            }
                        }
                    )
                }
            }

            // Botón para limpiar filtros (solo visible cuando hay filtros activos)
            if (selectedCategories.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = {
                        culturalCategories.forEach { category ->
                            if (selectedCategories.contains(category.id)) {
                                onCategoryDeselected(category.id)
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "Limpiar filtros",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = BlueYachay
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: CulturalCategory,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    val backgroundColor = if (isSelected) category.color else Color.Transparent
    val textColor = if (isSelected) category.textColor else category.color
    val borderColor = if (isSelected) category.color else category.color.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(backgroundColor)
            .clickable { onSelectionChanged(!isSelected) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// Versión alternativa con colores más suaves para el tema
val culturalCategoriesSoft = listOf(
    CulturalCategory(
        id = "GASTRONOMIA",
        displayName = "Gastronomía",
        color = RedYachay.copy(alpha = 0.1f),
        textColor = RedYachay
    ),
    CulturalCategory(
        id = "PATRIMONIO_ARQUEOLOGICO",
        displayName = "Patrimonio Arqueológico",
        color = BlueYachay.copy(alpha = 0.1f),
        textColor = BlueYachay
    ),
    CulturalCategory(
        id = "FLORA_MEDICINAL",
        displayName = "Flora Medicinal",
        color = GreenYachay.copy(alpha = 0.1f),
        textColor = GreenYachay
    ),
    CulturalCategory(
        id = "LEYENDAS_Y_TRADICIONES",
        displayName = "Leyendas y Tradiciones",
        color = YellowYachay.copy(alpha = 0.1f),
        textColor = YellowYachay
    )
)

@Composable
fun CategoryFilterCardSoft(
    selectedCategories: Set<String> = emptySet(),
    onCategorySelected: (String) -> Unit = {},
    onCategoryDeselected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CreamYachay),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtrar por categoría",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueYachay
                )

                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtrar",
                    tint = BlueYachay,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedCategories.isNotEmpty()) {
                Text(
                    text = "${selectedCategories.size} categoría(s) seleccionada(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(culturalCategoriesSoft) { category ->
                    CategoryChipSoft(
                        category = category,
                        isSelected = selectedCategories.contains(category.id),
                        onSelectionChanged = { selected ->
                            if (selected) {
                                onCategorySelected(category.id)
                            } else {
                                onCategoryDeselected(category.id)
                            }
                        }
                    )
                }
            }

            if (selectedCategories.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Limpiar filtros",
                    style = MaterialTheme.typography.bodySmall,
                    color = BlueYachay,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            culturalCategoriesSoft.forEach { category ->
                                if (selectedCategories.contains(category.id)) {
                                    onCategoryDeselected(category.id)
                                }
                            }
                        }
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun CategoryChipSoft(
    category: CulturalCategory,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    val backgroundColor = if (isSelected) category.color else Color.Transparent
    val textColor = if (isSelected) category.textColor else category.textColor.copy(alpha = 0.7f)
    val borderColor = if (isSelected) category.textColor else category.textColor.copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(backgroundColor)
            .clickable { onSelectionChanged(!isSelected) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun CategoryFilterCardPreview() {
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }

    CategoryFilterCard(
        selectedCategories = selectedCategories,
        onCategorySelected = { categoryId ->
            selectedCategories = selectedCategories + categoryId
        },
        onCategoryDeselected = { categoryId ->
            selectedCategories = selectedCategories - categoryId
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun CategoryFilterCardWithSelectionPreview() {
    var selectedCategories by remember { mutableStateOf(setOf("GASTRONOMIA", "FLORA_MEDICINAL")) }

    CategoryFilterCard(
        selectedCategories = selectedCategories,
        onCategorySelected = { categoryId ->
            selectedCategories = selectedCategories + categoryId
        },
        onCategoryDeselected = { categoryId ->
            selectedCategories = selectedCategories - categoryId
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun CategoryFilterCardSoftPreview() {
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }

    CategoryFilterCardSoft(
        selectedCategories = selectedCategories,
        onCategorySelected = { categoryId ->
            selectedCategories = selectedCategories + categoryId
        },
        onCategoryDeselected = { categoryId ->
            selectedCategories = selectedCategories - categoryId
        }
    )
}