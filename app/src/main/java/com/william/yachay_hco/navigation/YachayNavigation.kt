package com.william.yachay_hco.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.screens.*
import com.william.yachay_hco.ui.screens.cultural_analysis_screen.CulturalAnalysisScreen
import com.william.yachay_hco.view.compose.auth.LoginScreen
import com.william.yachay_hco.view.compose.profile.ProfileScreen
import com.william.yachay_hco.view.compose.profile.SuccessScreen

@Composable
fun YachayNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        // Pantalla de login
        composable("auth") {
            LoginScreen(
                navigateToHome = { isAdmin ->
                    navController.navigate("main?isAdmin=$isAdmin") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
            )
        }

        // Pantalla principal
        composable(
            "main?isAdmin={isAdmin}",
            arguments = listOf(navArgument("isAdmin") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { backStackEntry ->
            val isAdmin = backStackEntry.arguments?.getBoolean("isAdmin") ?: false
            MainScreen(
                onNavigateToCultural = { navController.navigate("cultural") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToReports = {
                    // CAMBIAR: Navegar según el rol
                    if (isAdmin) {
                        navController.navigate("admin_reports")
                    } else {
                        navController.navigate("reports")
                    }
                },
                onNavigateToDetail = { id ->
                    Log.d("MainScreen", "Navegando a detalle con ID: $id")
                    navController.navigate("cultural_detail/$id")
                },
                isAdmin = isAdmin
            )
        }

        // Pantalla de análisis cultural - CORREGIDO
        composable("cultural") {
            CulturalAnalysisScreen(
                onNavigateToDetail = { itemId ->
                    navController.navigate("cultural_detail/$itemId")
                },
                onNavigateToHome = {
                    navController.navigate("main") {
                        popUpTo("cultural") { inclusive = false }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile") {
                        popUpTo("cultural") { inclusive = false }
                    }
                }
            )
        }

        // Pantalla de detalles culturales
        composable(
            route = "cultural_detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            CulturalDetailScreen(
                itemId = itemId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Pantalla de éxito
        composable("success") {
            SuccessScreen(
                navigateToHome = {
                    navController.navigate("main") {
                        popUpTo("success") { inclusive = true }
                    }
                }
            )
        }

        // Perfil del usuario
        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate("editProfile") },
                onNavigateToStats = { navController.navigate("profileStats") },
                onLogout = {
                    navController.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de reportes
        composable("reports") {
            ReportScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 🔹 Nueva pantalla de reportes de administrador
        composable("admin_reports") {
            AdminReportScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}