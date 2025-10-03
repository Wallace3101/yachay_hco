package com.william.yachay_hco.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                navigateToHome = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                navigateToRegister = {
                    navController.navigate("register")
                },
                navigateToForgotPassword = {
                    navController.navigate("forgotPassword")
                }
            )
        }

        // Pantalla principal
        composable("main") {
            MainScreen(
                onNavigateToCultural = { navController.navigate("cultural") },
                onNavigateToProfile = { navController.navigate("profile") },
                onLogout = {
                    navController.navigate("auth") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de análisis cultural - CORREGIDO
        composable("cultural") {
            CulturalAnalysisScreen(
                onNavigateToDetail = { itemId ->
                    navController.navigate("cultural_detail/$itemId")
                },
                onNavigateBack = { navController.popBackStack() },
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
        composable("cultural_detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull()
            CulturalDetailScreen(
                culturalItem = createMockCulturalItem(), // Reemplazar con datos reales
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
                onNavigateToStats = { navController.navigate("profileStats") }
            )
        }
    }
}

// Función temporal para crear un item mock
private fun createMockCulturalItem() = CulturalItem(
    id = 1,
    titulo = "Pachamanca Huanuqueña",
    categoria = CulturalCategory.GASTRONOMIA,
    confianza = 0.92f,
    descripcion = "La pachamanca es un plato tradicional que consiste en carnes y verduras cocidas bajo tierra con piedras calientes. Es una técnica ancestral que se ha transmitido de generación en generación.",
    contexto_cultural = "La pachamanca es más que un plato; es un ritual que fortalece los vínculos comunitarios y familiares. Se prepara en ocasiones especiales y celebraciones importantes.",
    periodo_historico = "Época preinca - actualidad",
    ubicacion = "Toda la región de Huánuco, especialmente en zonas rurales",
    significado = "Representa la conexión con la Pachamama (Madre Tierra) y simboliza la unión de la comunidad en torno a la alimentación.",
    imagen = "",
    createdAt = "",
    updatedAt = "",
)