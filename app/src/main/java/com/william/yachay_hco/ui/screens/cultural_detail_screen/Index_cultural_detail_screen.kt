package com.william.yachay_hco.ui.screens.cultural_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.william.yachay_hco.ui.screens.cultural_detail_screen.partes.*
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
                    // Sección de imagen principal
                    ModernHeroSection(item = item)

                    // Contenido
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Header con título y confianza
                        ModernHeaderSection(item = item)

                        // Descripción
                        if (item.descripcion.isNotBlank()) {
                            ModernDescriptionCard(item.descripcion)
                        }

                        // Grid de info (ubicación, etc.)
                        ModernInfoGrid(item = item)

                        // Contexto cultural
                        if (item.contexto_cultural.isNotBlank()) {
                            ModernDetailCard(
                                title = "Contexto Cultural",
                                content = item.contexto_cultural,
                                icon = androidx.compose.material.icons.Icons.Default.Public,
                                accentColor = com.william.yachay_hco.ui.theme.GreenYachay
                            )
                        }

                        // Período histórico
                        if (item.periodo_historico.isNotBlank()) {
                            ModernDetailCard(
                                title = "Período Histórico",
                                content = item.periodo_historico,
                                icon = androidx.compose.material.icons.Icons.Default.History,
                                accentColor = com.william.yachay_hco.ui.theme.YellowYachay
                            )
                        }

                        // Significado
                        if (item.significado.isNotBlank()) {
                            ModernDetailCard(
                                title = "Significado Cultural",
                                content = item.significado,
                                icon = androidx.compose.material.icons.Icons.Default.Psychology,
                                accentColor = com.william.yachay_hco.ui.theme.RedYachay
                            )
                        }

                        // Metadata (fecha, nivel de confianza, etc.)
                        ModernMetadataCard(item = item)

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            } ?: EmptyState()
        }

        // Botón flotante de volver
        FloatingBackButton(
            onClick = onNavigateBack,
            scrollOffset = scrollState.value
        )
    }
}
