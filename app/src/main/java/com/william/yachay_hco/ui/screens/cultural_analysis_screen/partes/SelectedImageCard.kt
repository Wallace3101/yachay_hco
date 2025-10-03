package com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.*

@Composable
fun SelectedImageCard(
    bitmap: Bitmap,
    isAnalyzing: Boolean,
    onAnalyzeClick: () -> Unit,
    onRemoveImage: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Imagen seleccionada",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueYachay
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen
            Surface(
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shadowElevation = 2.dp
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Analizar
                Button(
                    onClick = onAnalyzeClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    enabled = !isAnalyzing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RedYachay,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    if (isAnalyzing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Analizando...",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Analizar Imagen",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Opción para cambiar imagen
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = onRemoveImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isAnalyzing
            ) {
                Icon(
                    Icons.Default.Photo,
                    contentDescription = null,
                    tint = GreenYachay,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Seleccionar otra imagen",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = GreenYachay
                )
            }
        }
    }
}