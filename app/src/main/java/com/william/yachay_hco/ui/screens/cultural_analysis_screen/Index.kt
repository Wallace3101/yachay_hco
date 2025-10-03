package com.william.yachay_hco.ui.screens.cultural_analysis_screen

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    onNavigateBack: () -> Unit,
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

    // Estados para manejo de errores y permisos
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

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 12.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .shadow(8.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                listOf("Inicio", "Cultural", "Perfil").forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = index == 1, // Cultural está seleccionado (índice 1)
                        onClick = {
                            when (index) {
                                0 -> onNavigateToHome()
                                2 -> onNavigateToProfile()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.Home
                                    1 -> Icons.Default.Explore
                                    else -> Icons.Default.Person
                                },
                                contentDescription = label,
                                tint = if (index == 1) BlueYachay else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                label,
                                color = if (index == 1) BlueYachay else Color.Gray,
                                fontWeight = if (index == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BlueYachay,
                            selectedTextColor = BlueYachay,
                            indicatorColor = BlueYachay.copy(0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Fondo con gradiente
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                CreamYachay.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Espaciado superior
                item { Spacer(modifier = Modifier.height(16.dp)) }

                // Encabezado con diseño mejorado
                item {
                    CulturalAnalysisHeader()
                }

                // Filtros por categoría con diseño mejorado
                item {
                    CategoryFilterCard()
                }

                // Botones para capturar/seleccionar imagen con diseño mejorado
                item {
                    ImageCaptureCard(
                        onGalleryClick = { imagePickerLauncher.launch("image/*") },
                        onCameraClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
                    )
                }

                // Imagen seleccionada con diseño mejorado
                selectedImageBitmap?.let { bitmap ->
                    item {
                        SelectedImageCard(
                            bitmap = bitmap,
                            isAnalyzing = uiState.isAnalyzing,
                            onAnalyzeClick = {
                                Log.d("CulturalAnalysis", "Botón Analizar presionado")
                                val base64 = bitmapToBase64(bitmap)
                                Log.d("CulturalAnalysis", "Base64 generado, length: ${base64.length}")
                                viewModel.analyzeImage(base64)
                            },
                            onRemoveImage = {
                                // Limpiar la imagen seleccionada
                                selectedImageBitmap = null
                                selectedImageUri = null
                                // También limpiar cualquier resultado de análisis previo
                                viewModel.clearAnalysisResult()
                                Log.d("CulturalAnalysis", "Imagen eliminada")
                            }
                        )
                    }
                }

                // Resultado del análisis con diseño mejorado
                analysisResult?.let { result ->
                    item {
                        AnalysisResultCard(
                            culturalItem = result,
                            onSave = { viewModel.saveCulturalItem(result) },
                            isSaving = uiState.isSaving
                        )
                    }
                }

                // Lista de elementos culturales
                item {
                    CulturalItemsHeader()
                }

                if (uiState.isLoading) {
                    item {
                        LoadingCard()
                    }
                }

                items(culturalItems) { item ->
                    CulturalItemCard(culturalItem = item)
                }

                // Espaciado inferior
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }

    // Manejar errores
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            errorMessage = error
            showErrorDialog = true
            Log.e("CulturalAnalysis", "Error mostrado: $error")
        }
    }

    // Mensaje de guardado exitoso
    if (uiState.itemSaved) {
        LaunchedEffect(Unit) {
            Log.d("CulturalAnalysis", "Item guardado exitosamente")
            viewModel.resetSaveState()
            viewModel.clearAnalysisResult()
        }
    }

    // Dialogs
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