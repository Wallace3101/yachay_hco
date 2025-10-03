package com.william.yachay_hco.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.AuthResult
import com.william.yachay_hco.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    onNavigateBack: () -> Unit,
    onSaveComplete: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authResult.collectAsStateWithLifecycle()

    val currentUser = when (authState) {
        is AuthResult.Success -> (authState as AuthResult.Success).user
        else -> null
    }
    var username by remember { mutableStateOf(currentUser?.username ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Huánuco, Perú") }

    var isLoading by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { showSaveDialog = true },
                        enabled = !isLoading && username.isNotBlank() && email.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = BlueYachay
                            )
                        } else {
                            Text("Guardar", color = BlueYachay, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CreamYachay
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(CreamYachay, Color.White)
                    )
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar de perfil
            ProfileAvatarSection(
                username = username,
                onEditAvatar = { /* TODO: Implementar cambio de avatar */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Formulario de edición
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Información básica
                SectionCard(title = "Información Básica") {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Nombre de usuario") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueYachay,
                            focusedLabelColor = BlueYachay
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueYachay,
                            focusedLabelColor = BlueYachay
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BlueYachay,
                                focusedLabelColor = BlueYachay
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Apellido") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BlueYachay,
                                focusedLabelColor = BlueYachay
                            ),
                            singleLine = true
                        )
                    }
                }

                // Información adicional
                SectionCard(title = "Información Adicional") {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Ubicación") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueYachay,
                            focusedLabelColor = BlueYachay
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Biografía") },
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BlueYachay,
                            focusedLabelColor = BlueYachay
                        ),
                        maxLines = 3,
                        placeholder = { Text("Cuéntanos algo sobre ti...") }
                    )
                }

                // Preferencias culturales
                SectionCard(title = "Intereses Culturales") {
                    Text(
                        text = "Selecciona tus categorías de interés",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InterestsSelector()
                }

                // Configuración de privacidad
                SectionCard(title = "Privacidad") {
                    var isPublicProfile by remember { mutableStateOf(false) }
                    var shareAnalytics by remember { mutableStateOf(true) }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Perfil público",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Permitir que otros vean tu perfil",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = isPublicProfile,
                            onCheckedChange = { isPublicProfile = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = GreenYachay
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Compartir estadísticas",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Ayudar a mejorar la app con datos anónimos",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = shareAnalytics,
                            onCheckedChange = { shareAnalytics = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = GreenYachay
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Diálogo de confirmación para guardar
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Guardar Cambios") },
            text = { Text("¿Deseas guardar los cambios realizados en tu perfil?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        isLoading = true
                        showSaveDialog = false
                        // TODO: Implementar guardado
                        // Simular guardado
                        kotlinx.coroutines.GlobalScope.launch {
                            kotlinx.coroutines.delay(1500)
                            onSaveComplete()
                        }
                    }
                ) {
                    Text("Guardar", color = BlueYachay)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun ProfileAvatarSection(
    username: String,
    onEditAvatar: () -> Unit
) {
    Box(
        modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(BlueYachay, BlueYachay.copy(alpha = 0.7f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.firstOrNull()?.toString()?.uppercase() ?: "U",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Botón de editar
            FloatingActionButton(
                onClick = onEditAvatar,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(40.dp),
                containerColor = GreenYachay,
                contentColor = Color.White
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Cambiar avatar",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BlueYachay,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun InterestsSelector() {
    val interests = listOf(
        "Gastronomía" to Icons.Default.Restaurant,
        "Patrimonio Arqueológico" to Icons.Default.AccountBalance,
        "Flora Medicinal" to Icons.Default.Eco,
        "Leyendas y Tradiciones" to Icons.Default.AutoStories,
        "Arte Popular" to Icons.Default.Palette,
        "Música Folclórica" to Icons.Default.MusicNote
    )

    val selectedInterests = remember { mutableStateListOf<String>() }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        interests.chunked(2).forEach { rowInterests ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowInterests.forEach { (interest, icon) ->
                    val isSelected = selectedInterests.contains(interest)

                    FilterChip(
                        onClick = {
                            if (isSelected) {
                                selectedInterests.remove(interest)
                            } else {
                                selectedInterests.add(interest)
                            }
                        },
                        label = { Text(interest) },
                        selected = isSelected,
                        leadingIcon = {
                            Icon(
                                icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GreenYachay.copy(alpha = 0.2f),
                            selectedLabelColor = GreenYachay,
                            selectedLeadingIconColor = GreenYachay
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Si solo hay un elemento en la fila, agregar espacio
                if (rowInterests.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}