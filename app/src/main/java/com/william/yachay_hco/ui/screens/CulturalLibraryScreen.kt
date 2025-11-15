package com.william.yachay_hco.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.CulturalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CulturalLibraryScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CulturalViewModel = hiltViewModel()
) {
    val culturalItems by viewModel.culturalItems.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedCategory by remember { mutableStateOf<CulturalCategory?>(null) }
    var isGridView by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredItems = remember(culturalItems, selectedCategory, searchQuery) {
        culturalItems.filter { item ->
            val matchesCategory = selectedCategory == null || item.categoria == selectedCategory
            val matchesSearch = searchQuery.isEmpty() ||
                    item.titulo.contains(searchQuery, ignoreCase = true) ||
                    item.descripcion.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biblioteca Cultural") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isGridView = !isGridView }
                    ) {
                        Icon(
                            if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                            contentDescription = if (isGridView) "Vista lista" else "Vista cuadrícula"
                        )
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
                .background(Color.White),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Barra de búsqueda
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Filtros por categoría
            item {
                CategoryFilters(
                    selectedCategory = selectedCategory,
                    onCategorySelected = {
                        selectedCategory = it
                        viewModel.filterByCategory(it)
                    }
                )
            }

            // Estadísticas de la biblioteca
            item {
                LibraryStats(
                    totalItems = culturalItems.size,
                    filteredItems = filteredItems.size,
                    categories = culturalItems.groupBy { it.categoria }.size
                )
            }

            // Lista/Grid de elementos culturales
            item {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BlueYachay)
                    }
                } else if (filteredItems.isEmpty()) {
                    EmptyLibraryState(
                        hasItems = culturalItems.isNotEmpty(),
                        searchQuery = searchQuery
                    )
                } else {
                    if (isGridView) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.height(600.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredItems) { item ->
                                LibraryItemCard(
                                    culturalItem = item,
                                    onClick = { onNavigateToDetail(item.id ?: 0) },
                                    isGridView = true
                                )
                            }
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            filteredItems.forEach { item ->
                                LibraryItemCard(
                                    culturalItem = item,
                                    onClick = { onNavigateToDetail(item.id ?: 0) },
                                    isGridView = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Buscar elementos culturales...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                }
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BlueYachay,
            focusedLabelColor = BlueYachay
        )
    )
}

@Composable
fun CategoryFilters(
    selectedCategory: CulturalCategory?,
    onCategorySelected: (CulturalCategory?) -> Unit
) {
    Column {
        Text(
            text = "Filtrar por categoría",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = BlueYachay
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            item {
                FilterChip(
                    onClick = { onCategorySelected(null) },
                    label = { Text("Todas") },
                    selected = selectedCategory == null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BlueYachay,
                        selectedLabelColor = Color.White
                    )
                )
            }

            items(CulturalCategory.values()) { category ->
                FilterChip(
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.displayName) },
                    selected = selectedCategory == category,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getCategoryColor(category),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun LibraryStats(
    totalItems: Int,
    filteredItems: Int,
    categories: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = GreenYachay.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Reemplazar en LibraryStats:
            StatItem(
                icon = Icons.Default.Collections,
                value = totalItems.toString(),
                label = "Total",
                color = BlueYachay
            )

            StatItem(
                icon = Icons.Default.Visibility,
                value = filteredItems.toString(),
                label = "Mostrados",
                color = GreenYachay
            )

            StatItem(
                icon = Icons.Default.Category,
                value = categories.toString(),
                label = "Categorías",
                color = YellowYachay
            )
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LibraryItemCard(
    culturalItem: CulturalItem,
    onClick: () -> Unit,
    isGridView: Boolean
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (isGridView) {
            // Vista de cuadrícula
            Column {
                // Imagen o placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(getCategoryColor(culturalItem.categoria).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!culturalItem.imagen.isNullOrEmpty()) {
                        AsyncImage(
                            model = culturalItem.imagen,
                            contentDescription = culturalItem.titulo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {

                    }
                }

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = culturalItem.titulo,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = culturalItem.categoria.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = getCategoryColor(culturalItem.categoria)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = culturalItem.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        } else {
            // Vista de lista
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Thumbnail
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(getCategoryColor(culturalItem.categoria).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!culturalItem.imagen.isNullOrEmpty()) {
                        AsyncImage(
                            model = culturalItem.imagen,
                            contentDescription = culturalItem.titulo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {

                    }
                }

                // Contenido
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = culturalItem.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = culturalItem.categoria.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = getCategoryColor(culturalItem.categoria)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = culturalItem.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = culturalItem.ubicacion,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EmptyLibraryState(
    hasItems: Boolean,
    searchQuery: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            if (hasItems) Icons.Default.SearchOff else Icons.Default.CollectionsBookmark,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Gray.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (hasItems) {
                if (searchQuery.isNotEmpty()) {
                    "No se encontraron resultados para '$searchQuery'"
                } else {
                    "No hay elementos en esta categoría"
                }
            } else {
                "Tu biblioteca está vacía"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (hasItems) {
                "Intenta con otros términos de búsqueda o cambia el filtro"
            } else {
                "Comienza analizando imágenes culturales para construir tu colección"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

fun getCategoryColor(category: CulturalCategory): Color {
    return when (category) {
        CulturalCategory.GASTRONOMIA -> Color(0xFFFF7043)              // Naranja cálido
        CulturalCategory.PATRIMONIO_ARQUEOLOGICO -> Color(0xFF8D6E63)  // Marrón piedra
        CulturalCategory.FLORA_MEDICINAL -> Color(0xFF66BB6A)          // Verde medicinal
        CulturalCategory.LEYENDAS_Y_TRADICIONES -> Color(0xFF42A5F5)   // Azul narrativo
        CulturalCategory.FESTIVIDADES -> Color(0xFFFFC107)             // Amarillo festivo
        CulturalCategory.DANZA -> Color(0xFFE91E63)                    // Rosa vibrante
        CulturalCategory.MUSICA -> Color(0xFFAB47BC)                   // Morado melódico
        CulturalCategory.VESTIMENTA -> Color(0xFF5C6BC0)               // Azul índigo (ropa)
        CulturalCategory.ARTE_POPULAR -> Color(0xFFFF8A65)             // Naranja artístico
        CulturalCategory.NATURALEZA_CULTURAL -> Color(0xFF26A69A)      // Verde azulado (naturaleza)
        CulturalCategory.OTRO -> Color(0xFF9E9E9E)
    }
}