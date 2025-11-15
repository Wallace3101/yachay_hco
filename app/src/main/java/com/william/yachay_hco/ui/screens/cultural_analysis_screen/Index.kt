package com.william.yachay_hco.ui.screens.cultural_analysis_screen

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.william.yachay_hco.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.viewmodel.CulturalViewModel
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes.*

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CulturalAnalysisScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: CulturalViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val analysisResult by viewModel.analysisResult.collectAsStateWithLifecycle()
    val culturalItems by viewModel.culturalItems.collectAsStateWithLifecycle()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedCategory by remember { mutableStateOf<CulturalCategory?>(null) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Launchers
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            selectedImageBitmap = loadBitmapFromUri(context, it, maxWidth = 1024, maxHeight = 1024)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            selectedImageBitmap = it.resizeBitmap(maxWidth = 1024, maxHeight = 1024)
        }
        selectedImageUri = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            showPermissionDialog = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserCulturalItems() // Carga solo los del usuario actual
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // FONDO MODERNO CON PATRÓN CULTURAL
        ModernCulturalBackground()

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                ModernBottomNav(
                    selectedIndex = 1,
                    onNavigateToHome = onNavigateToHome,
                    onNavigateToProfile = onNavigateToProfile
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Header moderno y minimalista
                item { ModernCulturalHeader() }

                // Botones de captura con diseño premium
                item {
                    ModernImageCaptureCard(
                        onGalleryClick = { imagePickerLauncher.launch("image/*") },
                        onCameraClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
                    )
                }

                // Imagen seleccionada
                selectedImageBitmap?.let { bitmap ->
                    item {
                        ModernSelectedImageCard(
                            bitmap = bitmap,
                            isAnalyzing = uiState.isAnalyzing,
                            onAnalyzeClick = {
                                Log.d("CulturalAnalysis", "Botón Analizar presionado")
                                val base64 = bitmapToBase64(bitmap)
                                Log.d("CulturalAnalysis", "Base64 generado, length: ${base64.length}")
                                viewModel.analyzeImage(base64)
                            },
                            onRemoveImage = {
                                selectedImageBitmap = null
                                selectedImageUri = null
                                viewModel.clearAnalysisResult()
                                Log.d("CulturalAnalysis", "Imagen eliminada")
                            }
                        )
                    }
                }

                // Resultado del análisis
                analysisResult?.let { result ->
                    item {
                        ModernAnalysisResultCard(
                            culturalItem = result,
                            onSave = { viewModel.saveCulturalItem(result) },
                            isSaving = uiState.isSaving
                        )
                    }
                }

                // Lista de elementos culturales
                item { ModernCulturalItemsHeader() }

                if (uiState.isLoading) {
                    item { LoadingCard() }
                }

                items(culturalItems.take(5)) { item ->
                    ModernCulturalItemCard(
                        culturalItem = item,
                        onViewDetails = onNavigateToDetail
                    )
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }

    // Efectos y diálogos
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            errorMessage = error
            showErrorDialog = true
            Log.e("CulturalAnalysis", "Error mostrado: $error")
        }
    }

    if (uiState.itemSaved) {
        LaunchedEffect(Unit) {
            Log.d("CulturalAnalysis", "Item guardado exitosamente")
            viewModel.resetSaveState()
            viewModel.clearAnalysisResult()
            viewModel.loadUserCulturalItems()
        }
    }

    if (showErrorDialog && errorMessage.isNotEmpty()) {
        ErrorDialog(
            errorMessage = errorMessage,
            onDismiss = {
                showErrorDialog = false
                viewModel.clearError()
            }
        )
    }

    if (showPermissionDialog) {
        PermissionDialog(
            onDismiss = { showPermissionDialog = false }
        )
    }
}

// FONDO MODERNO CON PATRÓN CULTURAL
@Composable
private fun ModernCulturalBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // IMAGEN ÚNICA COMO FONDO COMPLETO
        Image(
            painter = painterResource(R.drawable.cultural_bg_2),
            contentDescription = "Fondo cultural completo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay sutil para mejorar legibilidad del contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 0.4f),
                            Color.White.copy(alpha = 0.2f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun ModernCulturalHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(BlueYachay, GreenYachay)
                    ),
                    RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(45.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Análisis Cultural",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = BlueYachay
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Descubre el patrimonio de Huánuco",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
private fun ModernImageCaptureCard(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Captura tu elemento cultural",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón Galería mejorado
                Card(
                    onClick = onGalleryClick,
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        GreenYachay,
                                        GreenYachay.copy(alpha = 0.8f)
                                    )
                                )
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.PhotoLibrary,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Text(
                                "Galería",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Botón Cámara mejorado
                Card(
                    onClick = onCameraClick,
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        BlueYachay,
                                        BlueYachay.copy(alpha = 0.8f)
                                    )
                                )
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Text(
                                "Cámara",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernSelectedImageCard(
    bitmap: Bitmap,
    isAnalyzing: Boolean,
    onAnalyzeClick: () -> Unit,
    onRemoveImage: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Imagen lista para analizar",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BlueYachay
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Imagen con sombra y borde
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Analizar destacado
            Button(
                onClick = onAnalyzeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                enabled = !isAnalyzing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedYachay
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 4.dp
                )
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Analizando...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Analizar Imagen",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón cambiar imagen
            TextButton(
                onClick = onRemoveImage,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isAnalyzing
            ) {
                Icon(
                    Icons.Default.Photo,
                    contentDescription = null,
                    tint = GreenYachay,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Seleccionar otra imagen",
                    color = GreenYachay,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ModernAnalysisResultCard(
    culturalItem: com.william.yachay_hco.model.CulturalItem,
    onSave: () -> Unit,
    isSaving: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(28.dp)) {
            // Header con badge premium
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "✨ Resultado del Análisis",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = BlueYachay
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Identificación completada",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // Badge de confianza mejorado
                Surface(
                    color = getConfidenceColor(culturalItem.confianza).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = getConfidenceColor(culturalItem.confianza),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${(culturalItem.confianza * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = getConfidenceColor(culturalItem.confianza)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título destacado
            Text(
                text = culturalItem.titulo,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Categoría con icono
            Surface(
                color = getCategoryColor(culturalItem.categoria).copy(alpha = 0.15f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (culturalItem.categoria) {
                            CulturalCategory.GASTRONOMIA -> Icons.Default.Restaurant
                            CulturalCategory.PATRIMONIO_ARQUEOLOGICO -> Icons.Default.AccountBalance
                            CulturalCategory.FLORA_MEDICINAL -> Icons.Default.LocalFlorist
                            CulturalCategory.LEYENDAS_Y_TRADICIONES -> Icons.Default.AutoStories
                            CulturalCategory.FESTIVIDADES -> Icons.Default.Celebration
                            CulturalCategory.DANZA -> Icons.Default.DirectionsRun
                            CulturalCategory.MUSICA -> Icons.Default.MusicNote
                            CulturalCategory.VESTIMENTA -> Icons.Default.Checkroom
                            CulturalCategory.ARTE_POPULAR -> Icons.Default.Palette
                            CulturalCategory.NATURALEZA_CULTURAL -> Icons.Default.Public
                            CulturalCategory.OTRO -> Icons.Default.Category
                        },
                        contentDescription = null,
                        tint = getCategoryColor(culturalItem.categoria),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = culturalItem.categoria.name.replace("_", " "),
                        style = MaterialTheme.typography.titleSmall,
                        color = getCategoryColor(culturalItem.categoria),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(24.dp))

            // Secciones de información mejoradas
            InfoSectionModern(
                icon = Icons.Default.Description,
                title = "Descripción",
                content = culturalItem.descripcion,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSectionModern(
                icon = Icons.Default.Info,
                title = "Contexto Cultural",
                content = culturalItem.contexto_cultural,
                color = GreenYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSectionModern(
                icon = Icons.Default.CalendarToday,
                title = "Periodo Histórico",
                content = culturalItem.periodo_historico,
                color = YellowYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSectionModern(
                icon = Icons.Default.LocationOn,
                title = "Ubicación",
                content = culturalItem.ubicacion,
                color = RedYachay
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSectionModern(
                icon = Icons.Default.Star,
                title = "Significado",
                content = culturalItem.significado,
                color = BlueYachay
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón guardar mejorado
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenYachay
                ),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 4.dp
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Guardando...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Guardar en Mi Colección",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
} // ← CIERRE CORRECTO de ModernAnalysisResultCard

// AHORA LAS DEMÁS FUNCIONES FUERA:

@Composable
private fun InfoSectionModern(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray,
            lineHeight = 24.sp,
            modifier = Modifier.padding(start = 50.dp)
        )
    }
}

@Composable
private fun ModernCulturalItemsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(6.dp, RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BlueYachay, GreenYachay)
                        ),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = "Elementos Descubiertos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = BlueYachay
                    )
                    Text(
                        text = "Tu colección cultural",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernBottomNav(
    selectedIndex: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                isSelected = selectedIndex == 0,
                onClick = onNavigateToHome
            )

            // Botón central destacado
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .shadow(12.dp, androidx.compose.foundation.shape.CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(BlueYachay, GreenYachay)
                        ),
                        androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Explore,
                    contentDescription = "Análisis",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                isSelected = selectedIndex == 2,
                onClick = onNavigateToProfile
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = if (isSelected) BlueYachay else Color.Gray,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) BlueYachay else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// Funciones auxiliares
private fun getCategoryColor(category: com.william.yachay_hco.model.CulturalCategory): Color {
    return when (category) {
        CulturalCategory.GASTRONOMIA -> Color(0xFFFF7043)              // Naranja cálido
        CulturalCategory.PATRIMONIO_ARQUEOLOGICO -> Color(0xFF8D6E63)  // Marrón piedra
        CulturalCategory.FLORA_MEDICINAL -> Color(0xFF66BB6A)          // Verde medicinal
        CulturalCategory.LEYENDAS_Y_TRADICIONES -> Color(0xFF42A5F5)   // Azul narrativo
        CulturalCategory.FESTIVIDADES -> Color(0xFFFFC107)             // Amarillo festivo
        CulturalCategory.DANZA -> Color(0xFFE91E63)                    // Rosa vibrante
        CulturalCategory.MUSICA -> Color(0xFFAB47BC)                   // Morado melódico
        CulturalCategory.VESTIMENTA -> Color(0xFF5C6BC0)               // Azul índigo (ropa)
        CulturalCategory.ARTE_POPULAR -> Color(0xFFFF8A65)             // Naranja artístico
        CulturalCategory.NATURALEZA_CULTURAL -> Color(0xFF26A69A)      // Verde azulado (naturaleza)
        CulturalCategory.OTRO -> Color(0xFF9E9E9E)
    }
}

private fun getConfidenceColor(confidence: Double): Color {
    return when {
        confidence >= 0.8 -> GreenYachay
        confidence >= 0.6 -> YellowYachay
        else -> RedYachay
    }
}