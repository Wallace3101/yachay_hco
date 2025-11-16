package com.william.yachay_hco.ui.screens.report_screen.partes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.william.yachay_hco.model.Report
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.RedYachay
import com.william.yachay_hco.ui.theme.YellowYachay
import com.william.yachay_hco.viewmodel.ReportViewModel

@Composable
fun MyReportsTab(viewModel: ReportViewModel) {
    val userReportsState by viewModel.userReportsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUserReports()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (userReportsState) {
            is ReportViewModel.UiState.Idle -> {
                // Estado inicial
            }
            is ReportViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    BlueYachay.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = BlueYachay,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Cargando reportes...",
                            color = BlueYachay,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            is ReportViewModel.UiState.Success -> {
                val reports = (userReportsState as ReportViewModel.UiState.Success<List<Report>>).data

                if (reports.isEmpty()) {
                    EmptyReportsMessage()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(reports) { report ->
                            ReportCard(report = report)
                        }
                    }
                }
            }
            is ReportViewModel.UiState.Error -> {
                ErrorMessage(
                    message = (userReportsState as ReportViewModel.UiState.Error).message,
                    onRetry = { viewModel.getUserReports() }
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor, icon) = when (status) {
        "PENDIENTE" -> Triple(YellowYachay.copy(alpha = 0.2f), YellowYachay, Icons.Default.Schedule)
        "APROBADO" -> Triple(GreenYachay.copy(alpha = 0.2f), GreenYachay, Icons.Default.CheckCircle)
        "RECHAZADO" -> Triple(RedYachay.copy(alpha = 0.2f), RedYachay, Icons.Default.Cancel)
        else -> Triple(Color.Gray.copy(alpha = 0.2f), Color.Gray, Icons.Default.Info)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(1.5.dp, textColor.copy(alpha = 0.3f)),
        modifier = Modifier.shadow(2.dp, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = when (status) {
                    "PENDIENTE" -> "Pendiente"
                    "APROBADO" -> "Aprobado"
                    "RECHAZADO" -> "Rechazado"
                    else -> status
                },
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ReportDetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = BlueYachay,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF333333),
            lineHeight = 20.sp
        )
    }
}