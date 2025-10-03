package com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.RedYachay

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = RedYachay,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Error",
                    color = RedYachay,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Entendido")
            }
        }
    )
}

@Composable
fun PermissionDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permiso Requerido") },
        text = { Text("Se necesita acceso a la cámara para tomar fotos.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Entendido")
            }
        }
    )
}