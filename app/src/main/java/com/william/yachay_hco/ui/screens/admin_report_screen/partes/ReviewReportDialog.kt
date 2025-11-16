package com.william.yachay_hco.ui.screens.admin_report_screen.partes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.william.yachay_hco.model.Report
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.RedYachay
import com.william.yachay_hco.viewmodel.ReportViewModel

@Composable
fun ReviewReportDialog(
    report: Report,
    reviewState: ReportViewModel.UiState<*>,
    onDismiss: () -> Unit,
    onApprove: (String?) -> Unit,
    onReject: (String?) -> Unit
) {
    var adminNotes by remember { mutableStateOf("") }
    var showApproveConfirm by remember { mutableStateOf(false) }
    var showRejectConfirm by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Título con icono (se mantiene)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RateReview,
                        contentDescription = "Revisar",
                        tint = BlueYachay,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Revisar Reporte",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BlueYachay
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    report.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notas del administrador
                OutlinedTextField(
                    value = adminNotes,
                    onValueChange = { adminNotes = it },
                    label = { Text("Notas (opcional)") },
                    placeholder = { Text("Agrega comentarios sobre tu decisión...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueYachay,
                        focusedLabelColor = BlueYachay
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Mensajes de confirmación
                if (showApproveConfirm) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = GreenYachay.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = GreenYachay
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "¿Aprobar este reporte?",
                                    fontWeight = FontWeight.Bold,
                                    color = GreenYachay
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "El elemento cultural será agregado al sistema.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (showRejectConfirm) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = RedYachay.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = RedYachay
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "¿Rechazar este reporte?",
                                    fontWeight = FontWeight.Bold,
                                    color = RedYachay
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "El usuario será notificado del rechazo.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Mensaje de error
                if (reviewState is ReportViewModel.UiState.Error) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = RedYachay.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = RedYachay)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                reviewState.message,
                                color = RedYachay,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cancelar
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = reviewState !is ReportViewModel.UiState.Loading
                    ) {
                        Text("Cancelar")
                    }

                    // Rechazar
                    Button(
                        onClick = {
                            if (!showRejectConfirm) {
                                showRejectConfirm = true
                                showApproveConfirm = false
                            } else {
                                onReject(adminNotes.ifBlank { null })
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RedYachay
                        ),
                        enabled = reviewState !is ReportViewModel.UiState.Loading
                    ) {
                        if (reviewState is ReportViewModel.UiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (showRejectConfirm) "Confirmar" else "Rechazar")
                        }
                    }

                    // Aprobar
                    Button(
                        onClick = {
                            if (!showApproveConfirm) {
                                showApproveConfirm = true
                                showRejectConfirm = false
                            } else {
                                onApprove(adminNotes.ifBlank { null })
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenYachay
                        ),
                        enabled = reviewState !is ReportViewModel.UiState.Loading
                    ) {
                        if (reviewState is ReportViewModel.UiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (showApproveConfirm) "Confirmar" else "Aprobar")
                        }
                    }
                }
            }
        }
    }
}