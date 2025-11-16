package com.william.yachay_hco.ui.screens.main_screen.partes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.william.yachay_hco.R
import com.william.yachay_hco.ui.theme.CreamYachay

@Composable
fun BackgroundMosaicPattern() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamYachay)
    ) {
        // MOSAICO CON LAS 4 IMÁGENES QUE CUBRE TODA LA PANTALLA
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(R.drawable.img_cultural_1),
                    contentDescription = "Patrimonio cultural 1",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(R.drawable.img_cultural_2),
                    contentDescription = "Patrimonio cultural 2",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
            Row(modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(R.drawable.img_cultural_3),
                    contentDescription = "Patrimonio cultural 3",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(R.drawable.img_cultural_4),
                    contentDescription = "Patrimonio cultural 4",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Overlay muy sutil para mejorar legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.4f),
                            Color.White.copy(alpha = 0.2f)
                        )
                    )
                )
        )
    }
}