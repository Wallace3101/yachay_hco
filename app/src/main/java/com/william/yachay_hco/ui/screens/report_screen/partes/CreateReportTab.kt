package com.william.yachay_hco.ui.screens.report_screen.partes

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.william.yachay_hco.model.CreateReportRequest
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.RedYachay
import com.william.yachay_hco.ui.theme.YellowYachay
import com.william.yachay_hco.utils.ImageUtils
import com.william.yachay_hco.viewmodel.ReportViewModel

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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header compacto
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    BlueYachay.copy(alpha = 0.1f),
                                    GreenYachay.copy(alpha = 0.1f)
                                )
                            ),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(BlueYachay.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Create,
                            contentDescription = null,
                            tint = BlueYachay,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Nuevo Reporte",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50),
                            fontSize = 18.sp
                        )
                        Text(
                            "Completa la información",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF78909C),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Tipo de Reporte
            item {
                CompactDropdownField(
                    label = "Tipo de Reporte",
                    value = if (reportType == "CORRECCION") "Corrección de análisis" else "Nuevo elemento cultural",
                    icon = Icons.Default.Assignment,
                    iconColor = BlueYachay,
                    expanded = showReportTypeDropdown,
                    onExpandedChange = { showReportTypeDropdown = it }
                ) {
                    DropdownMenu(
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

            // Título
            item {
                CompactTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = "Título",
                    placeholder = "Ej: Pachamanca Huanuqueña",
                    icon = Icons.Default.Title,
                    iconColor = BlueYachay
                )
            }

            // Categoría
            item {
                CompactDropdownField(
                    label = "Categoría",
                    value = getCategoryDisplay(categoria),
                    icon = Icons.Default.Category,
                    iconColor = GreenYachay,
                    expanded = showCategoryDropdown,
                    onExpandedChange = { showCategoryDropdown = it }
                ) {
                    DropdownMenu(
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

            // Motivo
            item {
                CompactTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = "Motivo del reporte",
                    placeholder = "Explica por qué reportas este elemento",
                    icon = Icons.Default.Description,
                    iconColor = Color(0xFFFF7043),
                    minLines = 2
                )
            }

            // Descripción
            item {
                CompactTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = "Descripción",
                    placeholder = "Describe el elemento cultural",
                    icon = Icons.Default.Description,
                    iconColor = Color(0xFF42A5F5),
                    minLines = 2
                )
            }

            // Contexto Cultural
            item {
                CompactTextField(
                    value = contextoCultural,
                    onValueChange = { contextoCultural = it },
                    label = "Contexto Cultural",
                    placeholder = "Contexto histórico y cultural",
                    icon = Icons.Default.Public,
                    iconColor = Color(0xFF26A69A),
                    minLines = 2
                )
            }

            // Período Histórico
            item {
                CompactTextField(
                    value = periodoHistorico,
                    onValueChange = { periodoHistorico = it },
                    label = "Período Histórico",
                    placeholder = "Ej: Época prehispánica",
                    icon = Icons.Default.History,
                    iconColor = Color(0xFF8D6E63)
                )
            }

            // Ubicación
            item {
                CompactTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    label = "Ubicación",
                    placeholder = "Ej: Huánuco, Perú",
                    icon = Icons.Default.LocationOn,
                    iconColor = Color(0xFFE91E63)
                )
            }

            // Significado
            item {
                CompactTextField(
                    value = significado,
                    onValueChange = { significado = it },
                    label = "Significado",
                    placeholder = "Significado cultural del elemento",
                    icon = Icons.Default.Lightbulb,
                    iconColor = Color(0xFFFFC107),
                    minLines = 2
                )
            }

            // Imagen
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                tint = YellowYachay,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Imagen (Opcional)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF37474F),
                                fontSize = 13.sp
                            )
                        }

                        if (selectedImageBitmap != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box {
                                    AsyncImage(
                                        model = selectedImageBitmap,
                                        contentDescription = "Imagen seleccionada",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    IconButton(
                                        onClick = {
                                            selectedImageUri = null
                                            selectedImageBitmap = null
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .background(RedYachay, CircleShape)
                                            .size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Eliminar",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = YellowYachay.copy(alpha = 0.9f)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Icon(
                                if (selectedImageBitmap == null) Icons.Default.AddAPhoto
                                else Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (selectedImageBitmap == null) "Seleccionar" else "Cambiar",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Mensajes de estado
            item {
                when (createReportState) {
                    is ReportViewModel.UiState.Success -> {
                        CompactStatusCard(
                            icon = Icons.Default.CheckCircle,
                            iconColor = GreenYachay,
                            title = "¡Éxito!",
                            message = "Reporte enviado. Será revisado por un administrador.",
                            backgroundColor = GreenYachay.copy(alpha = 0.1f)
                        )
                    }
                    is ReportViewModel.UiState.Error -> {
                        CompactStatusCard(
                            icon = Icons.Default.Error,
                            iconColor = RedYachay,
                            title = "Error",
                            message = (createReportState as ReportViewModel.UiState.Error).message,
                            backgroundColor = RedYachay.copy(alpha = 0.1f)
                        )
                    }
                    else -> {}
                }
            }

            // Botón de Enviar
            item {
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
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueYachay,
                        disabledContainerColor = BlueYachay.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    ),
                    enabled = createReportState !is ReportViewModel.UiState.Loading
                ) {
                    if (createReportState is ReportViewModel.UiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Enviando...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    } else {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Enviar Reporte",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Espaciado final
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// Componentes reutilizables compactos
@Composable
private fun CompactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    minLines: Int = 1
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF37474F),
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        placeholder,
                        fontSize = 13.sp,
                        color = Color(0xFF90A4AE)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = iconColor,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = iconColor.copy(alpha = 0.03f),
                    unfocusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth(),
                minLines = minLines,
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompactDropdownField(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF37474F),
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = onExpandedChange
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = iconColor,
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedContainerColor = iconColor.copy(alpha = 0.03f),
                        unfocusedContainerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
                )
                content()
            }
        }
    }
}

@Composable
private fun CompactStatusCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    message: String,
    backgroundColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, iconColor.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.labelLarge,
                    color = iconColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    message,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF37474F),
                    lineHeight = 16.sp,
                    fontSize = 12.sp
                )
            }
        }
    }
}