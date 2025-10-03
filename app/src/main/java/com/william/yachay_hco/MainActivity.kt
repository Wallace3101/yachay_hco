package com.william.yachay_hco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.william.yachay_hco.navigation.YachayNavigation
import com.william.yachay_hco.ui.theme.YACHAY_HCOTheme
import com.william.yachay_hco.view.compose.auth.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YACHAY_HCOTheme {
                YACHAYApp()
            }
        }
    }
}

@Composable
fun YACHAYApp() {
    val navController = rememberNavController()
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (showSplash) {
            delay(2000)
            showSplash = false
        }
    }

    if (showSplash) {
        SplashScreen {
            showSplash = false
            navController.navigate("auth") {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    } else {
        YachayNavigation(navController = navController)
    }
}