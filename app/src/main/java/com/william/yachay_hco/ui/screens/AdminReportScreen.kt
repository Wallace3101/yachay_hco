package com.william.yachay_hco.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// CAMBIO: Import para .paint
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
// CAMBIO: Import para painterResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
// CAMBIO: Import para tu R (asegúrate que la ruta sea correcta)
import com.william.yachay_hco.R
import com.william.yachay_hco.model.Report
import com.william.yachay_hco.model.ReportStatus
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.ReportViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportScreen(
    viewModel: ReportViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Pendientes", "Aprobados", "Rechazados", "Todos")

    // El scrollBehavior se mantiene para el TopAppBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(selectedTab) {
        val status = when (selectedTab) {
            0 -> "PENDIENTE"
            1 -> "APROBADO"
            2 -> "RECHAZADO"
            3 -> null
            else -> null
        }
        viewModel.getAllReports(status)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Administración de Reportes",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Revisar reportes de usuarios",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BlueYachay,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            // CAMBIO: Fondo aplicado en la Column principal
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .paint(
                    painter = painterResource(id = R.drawable.cultural_bg_2), // Usando tu drawable
                    contentScale = ContentScale.Crop
                )
        ) {
            // TabRow se mantiene igual (con fondo blanco sólido por defecto)
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = BlueYachay,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BlueYachay,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Este composable ahora es transparente
            AdminReportsContent(viewModel)
        }
    }
}

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

    // CAMBIO: Este Box ahora es transparente.
    // El fondo (AsyncImage y Surface) se ha eliminado
    // para dejar ver el fondo de la Column padre.
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (allReportsState) {
            is ReportViewModel.UiState.Idle -> {
                // Estado inicial
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
fun AdminReportStats(reports: List<Report>) {
    val pendientes = reports.count { it.status == ReportStatus.PENDIENTE }
    val aprobados = reports.count { it.status == ReportStatus.APROBADO }
    val rechazados = reports.count { it.status == ReportStatus.RECHAZADO }

    // CAMBIO: Mantenemos el fondo semi-transparente
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f) // Efecto esmerilado
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Estadísticas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BlueYachay
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Pendientes", pendientes, YellowYachay)
                StatItem("Aprobados", aprobados, GreenYachay)
                StatItem("Rechazados", rechazados, RedYachay)
            }
        }
    }
}

@Composable
fun StatItem(label: String, count: Int, color: Color) {
    // Sin cambios
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = color.copy(alpha = 0.2f),
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

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

@Composable
fun EmptyAdminReportsMessage() {
    // CAMBIO: Mantenemos el texto blanco para legibilidad
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

// Reutilizar funciones auxiliares existentes (sin cambios)
fun formatDateReportAdmin(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}

fun getCategoryDisplayAdmin(category: String): String {
    return when (category) {
        "GASTRONOMIA" -> "Gastronomía"
        "PATRIMONIO_ARQUEOLOGICO" -> "Patrimonio Arqueológico"
        "FLORA_MEDICINAL" -> "Flora Medicinal"
        "LEYENDAS_Y_TRADICIONES" -> "Leyendas y Tradiciones"
        "FESTIVIDADES" -> "Festividades"
        "DANZA" -> "Danza"
        "MUSICA" -> "Música"
        "VESTIMENTA" -> "Vestimenta"
        "ARTE_POPULAR" -> "Arte Popular"
        "NATURALEZA_CULTURAL" -> "Naturaleza/Cultural"
        "OTRO" -> "Otro"
        else -> category
    }
}