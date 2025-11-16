package com.william.yachay_hco.ui.screens.admin_report_screen.partes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.model.Report
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.viewmodel.ReportViewModel

@Composable
fun AdminReportsContent(viewModel: ReportViewModel) {
    val allReportsState by viewModel.allReportsState.collectAsState()
    val reviewState by viewModel.reviewReportState.collectAsState()
    var selectedReport by remember { mutableStateOf<Report?>(null) }
    var showReviewDialog by remember { mutableStateOf(false) }

    // Actualizar lista cuando se revisa un reporte
    LaunchedEffect(reviewState) {
        if (reviewState is ReportViewModel.UiState.Success) {
            viewModel.getAllReports()
            showReviewDialog = false
            selectedReport = null
            viewModel.resetReviewReportState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (allReportsState) {
            is ReportViewModel.UiState.Idle -> {
            }
            is ReportViewModel.UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = BlueYachay // Se mantiene color brillante
                )
            }
            is ReportViewModel.UiState.Success -> {
                val reports = (allReportsState as ReportViewModel.UiState.Success<List<Report>>).data

                if (reports.isEmpty()) {
                    // Mantenemos el mensaje con texto blanco
                    EmptyAdminReportsMessage()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Resumen estadístico (mantiene fondo semi-transparente)
                        item {
                            AdminReportStats(reports)
                        }

                        // Lista de reportes (mantiene fondo semi-transparente)
                        items(reports) { report ->
                            AdminReportCard(
                                report = report,
                                onReviewClick = {
                                    selectedReport = report
                                    showReviewDialog = true
                                }
                            )
                        }
                    }
                }
            }
            is ReportViewModel.UiState.Error -> {
                // Asumiendo que ErrorMessage usa texto claro
                ErrorMessage(
                    message = (allReportsState as ReportViewModel.UiState.Error).message,
                    onRetry = { viewModel.getAllReports() }
                )
            }
        }
    }

    // Diálogo de revisión (se mantiene igual)
    if (showReviewDialog && selectedReport != null) {
        ReviewReportDialog(
            report = selectedReport!!,
            reviewState = reviewState,
            onDismiss = {
                showReviewDialog = false
                selectedReport = null
            },
            onApprove = { notes ->
                viewModel.approveReport(selectedReport!!.id!!, notes)
            },
            onReject = { notes ->
                viewModel.rejectReport(selectedReport!!.id!!, notes)
            }
        )
    }
}

@Composable
fun EmptyAdminReportsMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                Icons.Default.AssignmentTurnedIn,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No hay reportes",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Los reportes de usuarios aparecerán aquí",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Column {
        Text(text = message)
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
