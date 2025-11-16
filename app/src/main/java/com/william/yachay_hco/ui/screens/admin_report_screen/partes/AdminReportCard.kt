package com.william.yachay_hco.ui.screens.admin_report_screen.partes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.william.yachay_hco.model.Report
import com.william.yachay_hco.model.ReportStatus
import com.william.yachay_hco.ui.screens.ReportDetailRow
import com.william.yachay_hco.ui.screens.StatusChip
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.CreamYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.YellowYachay

@Composable
fun AdminReportCard(
    report: Report,
    onReviewClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // CAMBIO: Mantenemos el fondo semi-transparente
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f) // Efecto esmerilado
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = report.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = report.reportedByEmail ?: "Usuario",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getCategoryDisplayAdmin(report.categoria.name),
                        style = MaterialTheme.typography.bodySmall,
                        color = GreenYachay,
                        fontWeight = FontWeight.Medium
                    )
                }

                StatusChip(status = report.status.name)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tipo de reporte y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (report.report_type.apiValue == "CORRECCION")
                            Icons.Default.Edit else Icons.Default.AddCircle,
                        contentDescription = null,
                        tint = BlueYachay,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = report.report_type.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Text(
                    text = formatDateReportAdmin(report.createdAt ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Contenido expandible
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(color = CreamYachay, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Motivo destacado
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = YellowYachay.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.ReportProblem,
                                    contentDescription = null,
                                    tint = YellowYachay,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "Motivo del Reporte",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = YellowYachay
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                report.motivo,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Detalles del elemento cultural
                    ReportDetailRow("Descripción", report.descripcion)
                    ReportDetailRow("Contexto Cultural", report.contexto_cultural)
                    ReportDetailRow("Período Histórico", report.periodo_historico)
                    ReportDetailRow("Ubicación", report.ubicacion)
                    ReportDetailRow("Significado", report.significado)

                    // Imagen si existe
                    if (report.imagen != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            AsyncImage(
                                model = report.imagen,
                                contentDescription = "Imagen del reporte",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Notas del admin si existen
                    if (report.admin_notes?.isNotEmpty() == true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = BlueYachay.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = BlueYachay,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Notas del Administrador",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = BlueYachay
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    report.admin_notes ?: "",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (report.reviewedByEmail != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Por: ${report.reviewedByEmail}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                }
                            }
                        }
                    }

                    // Botón de revisión solo para reportes pendientes
                    if (report.status == ReportStatus.PENDIENTE) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onReviewClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BlueYachay
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.RateReview, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Revisar Reporte")
                        }
                    }
                }
            }

            // Indicador de expansión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Contraer" else "Expandir",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}