package com.william.yachay_hco.ui.screens.main_screen

import androidx.compose.ui.tooling.preview.Preview
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.william.yachay_hco.ui.screens.main_screen.partes.*
import com.william.yachay_hco.viewmodel.CulturalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToCultural: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToReports: () -> Unit,
    isAdmin: Boolean = false,
    viewModel: CulturalViewModel = hiltViewModel(),
) {
    Log.d("MainScreen", "isAdmin value: $isAdmin")

    val culturalItems by viewModel.culturalItems.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadCulturalItems()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundMosaicPattern()

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
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

                HeroExplorationCard(onExploreClick = onNavigateToCultural)

                ModernStatsGrid(
                    totalAnalysis = culturalItems.size,
                    discoveredItems = culturalItems.count { it.confianza >= 0.8f }
                )

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

@Preview(showBackground = true)
@Composable
fun MainScreenModernPreview() {
    MaterialTheme {
        MainScreen(
            onNavigateToCultural = {},
            onNavigateToProfile = {},
            onNavigateToDetail = {},
            onNavigateToReports = {},
            isAdmin = false
        )
    }
}
