package com.william.yachay_hco.ui.screens.report_screen.partes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.william.yachay_hco.model.Report
import com.william.yachay_hco.model.ReportType
import com.william.yachay_hco.ui.screens.getCategoryColor
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.CreamYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.YellowYachay

@Composable
fun ReportCard(report: Report) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Barra de color lateral según categoría
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(if (expanded) 120.dp else 60.dp)
                    .background(
                        color = getCategoryColor(report.categoria),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Contenido principal
            Column(modifier = Modifier.weight(1f)) {
                // Título del reporte
                Text(
                    text = report.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF2C3E50),
                    maxLines = if (expanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Fecha y proyecto en una línea compacta
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF78909C),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatDateReport(report.created_at ?: ""),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF78909C),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Categoría con color de fondo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            getCategoryColor(report.categoria).copy(alpha = 0.15f),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Category,
                        contentDescription = null,
                        tint = getCategoryColor(report.categoria),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = getCategoryDisplay(report.categoria.name),
                        style = MaterialTheme.typography.labelSmall,
                        color = getCategoryColor(report.categoria),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Contenido expandible
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color(0xFFECEFF1)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Tipo de reporte
                        CompactDetailRow(
                            icon = if (report.report_type == ReportType.CORRECCION)
                                Icons.Default.Edit else Icons.Default.AddCircle,
                            label = "Tipo",
                            value = report.report_type.displayName,
                            iconColor = getCategoryColor(report.categoria)
                        )

                        if (report.motivo?.isNotEmpty() == true) {
                            CompactDetailRow(
                                icon = Icons.Default.Info,
                                label = "Motivo",
                                value = report.motivo ?: "",
                                iconColor = getCategoryColor(report.categoria)
                            )
                        }

                        if (report.descripcion?.isNotEmpty() == true) {
                            CompactDetailRow(
                                icon = Icons.Default.Description,
                                label = "Descripción",
                                value = report.descripcion ?: "",
                                iconColor = getCategoryColor(report.categoria)
                            )
                        }

                        if (report.admin_notes?.isNotEmpty() == true) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFFFFF9C4).copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        Color(0xFFFDD835).copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        "Notas del Administrador",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF57F17),
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        report.admin_notes ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF5D4037),
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Columna para estado y otros elementos
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Estado (chip compacto)
                StatusChipCompact(status = report.status.name)

                // Punto indicador de estado (opcional, para refuerzo visual)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = when (report.status.name) {
                                "PENDIENTE" -> Color(0xFFF57C00) // Naranja
                                "APROBADO" -> Color(0xFF43A047) // Verde brillante
                                "RECHAZADO" -> Color(0xFFE53935) // Rojo
                                else -> Color.Gray
                            },
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun CompactDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    iconColor: Color = Color(0xFF78909C)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(iconColor.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF90A4AE),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF37474F),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun StatusChipCompact(status: String) {
    val (bgColor, textColor, text) = when (status) {
        "PENDIENTE" -> Triple(
            Color(0xFFFFF3E0),
            Color(0xFFF57C00),
            "Pendiente"
        )
        "APROBADO" -> Triple(
            Color(0xFFE8F5E9),
            Color(0xFF43A047),
            "Aprobado"
        )
        "RECHAZADO" -> Triple(
            Color(0xFFFFEBEE),
            Color(0xFFE53935),
            "Rechazado"
        )
        else -> Triple(
            Color(0xFFECEFF1),
            Color(0xFF546E7A),
            status
        )
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}