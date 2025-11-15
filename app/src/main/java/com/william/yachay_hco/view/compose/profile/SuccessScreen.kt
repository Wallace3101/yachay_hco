package com.william.yachay_hco.view.compose.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.william.yachay_hco.R
import com.william.yachay_hco.ui.theme.CreamYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.YellowYachay

// view/compose/profile/SuccessScreen.kt
@Composable
fun SuccessScreen(navigateToHome: () -> Unit) {
    var scale by remember { mutableStateOf(0.5f) }

    LaunchedEffect(Unit) {
        scale = 1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenYachay),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_success),
                contentDescription = "Éxito",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "¡Felicidades!",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.scale(scale)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tu perfil ha sido configurado exitosamente",
                color = CreamYachay,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.scale(scale)
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = navigateToHome,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = YellowYachay,
                    contentColor = Color.Black
                )
            ) {
                Text("Comenzar", fontWeight = FontWeight.Bold)
            }
        }
    }
}