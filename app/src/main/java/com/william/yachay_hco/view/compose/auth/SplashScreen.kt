package com.william.yachay_hco.view.compose.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.william.yachay_hco.R
import com.william.yachay_hco.ui.theme.CreamYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import kotlinx.coroutines.delay

// view/compose/auth/SplashScreen.kt
@Composable
fun SplashScreen(navigateToLogin: () -> Unit) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(1000))
        delay(1500)
        navigateToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenYachay),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Reemplaza con ícono de Material temporalmente
            Icon(
                imageVector = Icons.Filled.Star, // o cualquier otro ícono
                contentDescription = "YACHAYHCO Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "YACHAY HCO",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.scale(scale.value)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Conectando con nuestro patrimonio",
                color = CreamYachay,
                fontSize = 14.sp,
                modifier = Modifier.scale(scale.value)
            )
        }
    }
}