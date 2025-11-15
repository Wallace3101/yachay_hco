package com.william.yachay_hco.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.william.yachay_hco.R
import com.william.yachay_hco.model.CreateReportRequest
import com.william.yachay_hco.model.Report
import com.william.yachay_hco.model.ReportType
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.utils.ImageUtils
import com.william.yachay_hco.viewmodel.ReportViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: ReportViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Mis Reportes", "Crear Reporte")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sistema de Reportes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BlueYachay,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                modifier = Modifier.shadow(8.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .paint(
                    painter = painterResource(id = R.drawable.cultural_bg_2),
                    contentScale = ContentScale.Crop
                )
        ) {
            // Tabs modernos con glassmorphism
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White.copy(alpha = 0.9f),
                contentColor = BlueYachay,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)),
                        color = BlueYachay,
                        height = 4.dp
                    )
                },
                modifier = Modifier.shadow(4.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                fontSize = if (selectedTab == index) 16.sp else 15.sp
                            )
                        },
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // Contenido según el tab seleccionado
            when (selectedTab) {
                0 -> MyReportsTab(viewModel)
                1 -> CreateReportTab(viewModel)
            }
        }
    }
}

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
fun ReportCard(report: Report) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .shadow(
                elevation = if (expanded) 12.dp else 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = BlueYachay.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column {
            // Header con gradiente sutil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                BlueYachay.copy(alpha = 0.05f),
                                GreenYachay.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = report.titulo,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            maxLines = if (expanded) Int.MAX_VALUE else 2,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    GreenYachay.copy(alpha = 0.15f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = null,
                                tint = GreenYachay,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = getCategoryDisplay(report.categoria.name),
                                style = MaterialTheme.typography.bodyMedium,
                                color = GreenYachay,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    StatusChip(status = report.status.name)
                }
            }

            // Contenido
            Column(modifier = Modifier.padding(20.dp)) {
                // Tipo de reporte con estilo moderno
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            BlueYachay.copy(alpha = 0.08f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(BlueYachay.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (report.report_type == ReportType.CORRECCION)
                                Icons.Default.Edit else Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = BlueYachay,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = report.report_type.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = BlueYachay,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Contenido expandible
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = CreamYachay.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        ReportDetailRow("Motivo", report.motivo ?: "Sin información")
                        ReportDetailRow("Descripción", report.descripcion ?: "Sin información")
                        ReportDetailRow("Contexto Cultural", report.contexto_cultural ?: "Sin información")
                        ReportDetailRow("Período Histórico", report.periodo_historico ?: "Sin información")
                        ReportDetailRow("Ubicación", report.ubicacion ?: "Sin información")
                        ReportDetailRow("Significado", report.significado ?: "Sin información")

                        if (report.admin_notes?.isNotEmpty() == true) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = YellowYachay.copy(alpha = 0.12f)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, YellowYachay.copy(alpha = 0.3f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(YellowYachay.copy(alpha = 0.3f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.Info,
                                                contentDescription = null,
                                                tint = YellowYachay,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "Notas del Administrador",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = YellowYachay
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        report.admin_notes ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    Color.Gray.copy(alpha = 0.1f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Creado: ${formatDateReport(report.createdAt ?: "")}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Indicador de expansión mejorado
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(
                            CreamYachay.copy(alpha = 0.3f),
                            RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Contraer" else "Expandir",
                        tint = BlueYachay,
                        modifier = Modifier.size(24.dp)
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportTab(viewModel: ReportViewModel) {
    val createReportState by viewModel.createReportState.collectAsState()
    val context = LocalContext.current

    var reportType by remember { mutableStateOf("CORRECCION") }
    var motivo by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("GASTRONOMIA") }
    var descripcion by remember { mutableStateOf("") }
    var contextoCultural by remember { mutableStateOf("") }
    var periodoHistorico by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("Huánuco, Perú") }
    var significado by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showReportTypeDropdown by remember { mutableStateOf(false) }

    val imageUtils = remember(context) { ImageUtils(context) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            selectedImageBitmap = imageUtils.uriToBitmap(context, it)
        }
    }

    LaunchedEffect(createReportState) {
        when (createReportState) {
            is ReportViewModel.UiState.Success -> {
                reportType = "CORRECCION"
                motivo = ""
                titulo = ""
                categoria = "GASTRONOMIA"
                descripcion = ""
                contextoCultural = ""
                periodoHistorico = ""
                ubicacion = "Huánuco, Perú"
                significado = ""
                selectedImageUri = null
                selectedImageBitmap = null

                viewModel.resetCreateReportState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                // Header del formulario
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = BlueYachay
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.shadow(8.dp, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Create,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Nuevo Reporte",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "Completa el formulario",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            item {
                // Tipo de Reporte
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Assignment,
                                contentDescription = null,
                                tint = BlueYachay,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Tipo de Reporte",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BlueYachay
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        ExposedDropdownMenuBox(
                            expanded = showReportTypeDropdown,
                            onExpandedChange = { showReportTypeDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = if (reportType == "CORRECCION") "Corrección de análisis" else "Nuevo elemento cultural",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Selecciona el tipo") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showReportTypeDropdown) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BlueYachay,
                                    focusedLabelColor = BlueYachay,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = showReportTypeDropdown,
                                onDismissRequest = { showReportTypeDropdown = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Corrección de análisis") },
                                    onClick = {
                                        reportType = "CORRECCION"
                                        showReportTypeDropdown = false
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Edit, null, tint = BlueYachay)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Nuevo elemento cultural") },
                                    onClick = {
                                        reportType = "NUEVO_ELEMENTO"
                                        showReportTypeDropdown = false
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.AddCircle, null, tint = GreenYachay)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                ModernTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = "Motivo del reporte",
                    placeholder = "Explica por qué reportas este elemento",
                    icon = Icons.Default.Description,
                    minLines = 3,
                    required = true
                )
            }

            item {
                ModernTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = "Título",
                    placeholder = "Ej: Pachamanca Huanuqueña",
                    icon = Icons.Default.Title,
                    required = true
                )
            }

            item {
                // Categoría
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Category,
                                contentDescription = null,
                                tint = GreenYachay,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Categoría",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GreenYachay
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        ExposedDropdownMenuBox(
                            expanded = showCategoryDropdown,
                            onExpandedChange = { showCategoryDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = getCategoryDisplay(categoria),
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Selecciona una categoría") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GreenYachay,
                                    focusedLabelColor = GreenYachay,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = showCategoryDropdown,
                                onDismissRequest = { showCategoryDropdown = false }
                            ) {
                                val categories = listOf(
                                    "GASTRONOMIA" to "Gastronomía",
                                    "PATRIMONIO_ARQUEOLOGICO" to "Patrimonio Arqueológico",
                                    "FLORA_MEDICINAL" to "Flora Medicinal",
                                    "LEYENDAS_Y_TRADICIONES" to "Leyendas y Tradiciones",
                                    "FESTIVIDADES" to "Festividades",
                                    "DANZA" to "Danza",
                                    "MUSICA" to "Música",
                                    "VESTIMENTA" to "Vestimenta",
                                    "ARTE_POPULAR" to "Arte Popular",
                                    "NATURALEZA_CULTURAL" to "Naturaleza/Cultural",
                                    "OTRO" to "Otro"
                                )
                                categories.forEach { (value, display) ->
                                    DropdownMenuItem(
                                        text = { Text(display) },
                                        onClick = {
                                            categoria = value
                                            showCategoryDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                ModernTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = "Descripción",
                    placeholder = "Describe el elemento cultural",
                    icon = Icons.Default.Description,
                    minLines = 3,
                    required = true
                )
            }

            item {
                ModernTextField(
                    value = contextoCultural,
                    onValueChange = { contextoCultural = it },
                    label = "Contexto Cultural",
                    placeholder = "Contexto histórico y cultural",
                    icon = Icons.Default.Public,
                    minLines = 3,
                    required = true
                )
            }

            item {
                ModernTextField(
                    value = periodoHistorico,
                    onValueChange = { periodoHistorico = it },
                    label = "Período Histórico",
                    placeholder = "Ej: Época prehispánica",
                    icon = Icons.Default.History,
                    required = true
                )
            }

            item {
                ModernTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    label = "Ubicación",
                    placeholder = "Ej: Huánuco, Perú",
                    icon = Icons.Default.LocationOn,
                    required = true
                )
            }

            item {
                ModernTextField(
                    value = significado,
                    onValueChange = { significado = it },
                    label = "Significado",
                    placeholder = "Significado cultural del elemento",
                    icon = Icons.Default.Lightbulb,
                    minLines = 3,
                    required = true
                )
            }

            item {
                // Imagen
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                tint = YellowYachay,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Imagen (Opcional)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = YellowYachay
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        if (selectedImageBitmap != null) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box {
                                    AsyncImage(
                                        model = selectedImageBitmap,
                                        contentDescription = "Imagen seleccionada",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    // Overlay con botón de eliminar
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Black.copy(alpha = 0.3f),
                                                        Color.Transparent,
                                                        Color.Transparent
                                                    )
                                                )
                                            )
                                    )
                                    IconButton(
                                        onClick = {
                                            selectedImageUri = null
                                            selectedImageBitmap = null
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(12.dp)
                                            .background(RedYachay, CircleShape)
                                            .shadow(4.dp, CircleShape)
                                            .size(40.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Eliminar imagen",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedImageBitmap == null)
                                    YellowYachay else YellowYachay.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            Icon(
                                if (selectedImageBitmap == null) Icons.Default.AddAPhoto
                                else Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                if (selectedImageBitmap == null) "Seleccionar Imagen"
                                else "Cambiar Imagen",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            item {
                // Botón de Enviar con diseño premium
                Button(
                    onClick = {
                        if (validateForm(motivo, titulo, descripcion, contextoCultural, periodoHistorico, ubicacion, significado)) {
                            val imageBase64 = selectedImageBitmap?.let {
                                imageUtils.bitmapToBase64(it)
                            }

                            val request = CreateReportRequest(
                                report_type = reportType,
                                motivo = motivo,
                                titulo = titulo,
                                categoria = categoria,
                                descripcion = descripcion,
                                contexto_cultural = contextoCultural,
                                periodo_historico = periodoHistorico,
                                ubicacion = ubicacion,
                                significado = significado,
                                imagen_base64 = imageBase64
                            )

                            viewModel.createReport(request)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(
                            elevation = if (createReportState is ReportViewModel.UiState.Loading) 0.dp else 8.dp,
                            shape = RoundedCornerShape(18.dp),
                            spotColor = BlueYachay.copy(alpha = 0.5f)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueYachay,
                        disabledContainerColor = BlueYachay.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(18.dp),
                    enabled = createReportState !is ReportViewModel.UiState.Loading
                ) {
                    if (createReportState is ReportViewModel.UiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Enviando...",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    } else {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Enviar Reporte",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }
            }

            // Mensajes de estado con diseño mejorado
            item {
                when (createReportState) {
                    is ReportViewModel.UiState.Success -> {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = GreenYachay.copy(alpha = 0.15f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, GreenYachay.copy(alpha = 0.3f)),
                            modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(GreenYachay.copy(alpha = 0.3f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = GreenYachay,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "¡Éxito!",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = GreenYachay,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Reporte enviado exitosamente. Será revisado por un administrador.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = GreenYachay.copy(alpha = 0.8f),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                    is ReportViewModel.UiState.Error -> {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = RedYachay.copy(alpha = 0.15f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(2.dp, RedYachay.copy(alpha = 0.3f)),
                            modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(RedYachay.copy(alpha = 0.3f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Error,
                                        contentDescription = null,
                                        tint = RedYachay,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Error",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = RedYachay,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        (createReportState as ReportViewModel.UiState.Error).message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = RedYachay.copy(alpha = 0.8f),
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    minLines: Int = 1,
    required: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = BlueYachay,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BlueYachay
                )
                if (required) {
                    Text(
                        " *",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = RedYachay
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                minLines = minLines,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BlueYachay,
                    focusedTextColor = Color(0xFF1A1A1A),
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = BlueYachay
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
fun EmptyReportsMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                BlueYachay.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Assignment,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = BlueYachay.copy(alpha = 0.4f)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "No tienes reportes",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1A1A1A),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Crea un reporte para sugerir correcciones\no nuevos elementos culturales",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                RedYachay.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ErrorOutline,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = RedYachay
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "¡Oops! Algo salió mal",
                style = MaterialTheme.typography.headlineSmall,
                color = RedYachay,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueYachay
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .height(52.dp)
                    .shadow(6.dp, RoundedCornerShape(14.dp)),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Reintentar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// Funciones auxiliares
fun getCategoryDisplay(category: String): String {
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

fun formatDateReport(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}

fun validateForm(
    motivo: String,
    titulo: String,
    descripcion: String,
    contextoCultural: String,
    periodoHistorico: String,
    ubicacion: String,
    significado: String
): Boolean {
    return motivo.isNotBlank() &&
            titulo.isNotBlank() &&
            descripcion.isNotBlank() &&
            contextoCultural.isNotBlank() &&
            periodoHistorico.isNotBlank() &&
            ubicacion.isNotBlank() &&
            significado.isNotBlank()
}