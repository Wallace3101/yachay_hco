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