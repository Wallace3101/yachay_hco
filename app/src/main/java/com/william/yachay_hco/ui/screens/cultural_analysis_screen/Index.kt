package com.william.yachay_hco.ui.screens.cultural_analysis_screen

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.william.yachay_hco.viewmodel.CulturalViewModel
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
    var showPermissionDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Launchers
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            selectedImageBitmap =
                loadBitmapFromUri(context, it, maxWidth = 1024, maxHeight = 1024)
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
        viewModel.loadUserCulturalItems()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        ModernCulturalBackground()

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
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

                // Header
                item { ModernCulturalHeader() }

                // Botones de captura
                item {
                    ModernImageCaptureCard(
                        onGalleryClick = { imagePickerLauncher.launch("image/*") },
                        onCameraClick = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
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
                                Log.d(
                                    "CulturalAnalysis",
                                    "Base64 generado, length: ${base64.length}"
                                )
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

    // Manejo de errores
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            errorMessage = error
            showErrorDialog = true
            Log.e("CulturalAnalysis", "Error mostrado: $error")
        }
    }

    // Item guardado
    if (uiState.itemSaved) {
        LaunchedEffect(Unit) {
            Log.d("CulturalAnalysis", "Item guardado exitosamente")
            viewModel.resetSaveState()
            viewModel.clearAnalysisResult()
            viewModel.loadUserCulturalItems()
        }
    }

    // Diálogos
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
