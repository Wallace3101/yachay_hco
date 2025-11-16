package com.william.yachay_hco.view.compose.profile.profile_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.view.compose.profile.profile_screen.partes.AnimatedAchievementsGrid
import com.william.yachay_hco.view.compose.profile.profile_screen.partes.AnimatedBackgroundPattern
import com.william.yachay_hco.view.compose.profile.profile_screen.partes.EnhancedStatsPager
import com.william.yachay_hco.view.compose.profile.profile_screen.partes.GlassmorphicProfileHeader
import com.william.yachay_hco.view.compose.profile.profile_screen.partes.GlassmorphicSettingsCard
import com.william.yachay_hco.view.compose.profile.profile_screen.partes.InteractiveCategoriesSection
import com.william.yachay_hco.view.compose.profile.profile_screen.partes.ModernRecentActivityCard
import com.william.yachay_hco.view.compose.profile.profile_screen.partes.ModernTopBar
import com.william.yachay_hco.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToStats: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val currentUser by profileViewModel.userState.collectAsStateWithLifecycle()
    val userStats by profileViewModel.userStats.collectAsStateWithLifecycle()
    val culturalItems by profileViewModel.culturalItems.collectAsStateWithLifecycle()

    val displayName = when {
        !currentUser?.firstName.isNullOrBlank() && !currentUser?.lastName.isNullOrBlank() ->
            "${currentUser?.firstName} ${currentUser?.lastName}"
        !currentUser?.firstName.isNullOrBlank() -> currentUser?.firstName
        !currentUser?.lastName.isNullOrBlank() -> currentUser?.lastName
        else -> currentUser?.username ?: "Explorador Cultural"
    }

    LaunchedEffect(Unit) {
        profileViewModel.refreshUser()
    }

    LaunchedEffect(userStats) {
        Log.d(
            "ProfileScreen",
            "Stats updated - Total: ${userStats.totalAnalysis}, Weekly: ${userStats.weeklyAnalysis}"
        )
    }

    LaunchedEffect(culturalItems) {
        Log.d("ProfileScreen", "Cultural items loaded: ${culturalItems.size}")
        culturalItems.forEach { item ->
            Log.d(
                "ProfileScreen",
                "Item: ${item.titulo}, Created by: ${item.created_by}, Date: ${item.created_at}"
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BlueYachay.copy(alpha = 0.05f),
                        GreenYachay.copy(alpha = 0.02f),
                        Color.White
                    )
                )
            )
    ) {
        AnimatedBackgroundPattern()   // <- viene de partes/Background.kt

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                ModernTopBar(       // <- viene de partes/TopBar.kt
                    onNavigateBack = onNavigateBack,
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    GlassmorphicProfileHeader( // <- partes/ProfileHeader.kt
                        user = currentUser,
                        displayName = displayName,
                        onEditClick = onNavigateToEdit
                    )
                }

                item {
                    EnhancedStatsPager(        // <- partes/StatsSection.kt
                        userStats = userStats,
                        onStatsClick = onNavigateToStats
                    )
                }

                item {
                    AnimatedAchievementsGrid(  // <- partes/AchievementsSection.kt
                        totalAnalysis = userStats.totalAnalysis
                    )
                }

                item {
                    ModernRecentActivityCard(  // <- partes/RecentActivitySection.kt
                        recentItems = culturalItems.take(3)
                    )
                }

                item {
                    InteractiveCategoriesSection( // <- partes/CategoriesSection.kt
                        culturalItems = culturalItems
                    )
                }

                item {
                    GlassmorphicSettingsCard(  // <- partes/SettingsSection.kt
                        onLogout = onLogout
                    )
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
