package com.william.yachay_hco.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var autoBackup by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(false) }
    var highQualityImages by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CreamYachay
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de cuenta
            item {
                SettingsSection(title = "Cuenta") {
                    SettingsItem(
                        icon = Icons.Default.Person,
                        title = "Editar Perfil",
                        description = "Cambiar información personal",
                        onClick = { }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Cambiar Contraseña",
                        description = "Actualizar credenciales de acceso",
                        onClick = { }
                    )
                }
            }

            // Sección de notificaciones
            item {
                SettingsSection(title = "Notificaciones") {
                    SwitchSettingsItem(
                        icon = Icons.Default.Notifications,
                        title = "Notificaciones Push",
                        description = "Recibir alertas de nuevos análisis",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Schedule,
                        title = "Horarios de Notificación",
                        description = "Configurar cuándo recibir alertas",
                        onClick = { },
                    )
                }
            }

            // Sección de análisis
            item {
                SettingsSection(title = "Análisis Cultural") {
                    SwitchSettingsItem(
                        icon = Icons.Default.HighQuality,
                        title = "Imágenes de Alta Calidad",
                        description = "Usar máxima resolución para análisis más precisos",
                        checked = highQualityImages,
                        onCheckedChange = { highQualityImages = it }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SwitchSettingsItem(
                        icon = Icons.Default.CloudSync,
                        title = "Respaldo Automático",
                        description = "Guardar análisis en la nube automáticamente",
                        checked = autoBackup,
                        onCheckedChange = { autoBackup = it }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Api,
                        title = "Configuración de API",
                        description = "Gestionar conexiones y claves de API",
                        onClick = { }
                    )
                }
            }

            // Sección de interfaz
            item {
                SettingsSection(title = "Interfaz") {
                    SwitchSettingsItem(
                        icon = Icons.Default.DarkMode,
                        title = "Modo Oscuro",
                        description = "Usar tema oscuro para la aplicación",
                        checked = darkMode,
                        onCheckedChange = { darkMode = it }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Language,
                        title = "Idioma",
                        description = "Español (Perú)",
                        onClick = { }
                    )
                }
            }

            // Sección de datos y privacidad
            item {
                SettingsSection(title = "Datos y Privacidad") {
                    SettingsItem(
                        icon = Icons.Default.Storage,
                        title = "Gestión de Almacenamiento",
                        description = "Ver y limpiar datos locales",
                        onClick = { }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Download,
                        title = "Exportar Datos",
                        description = "Descargar copia de tus análisis",
                        onClick = { }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Delete,
                        title = "Eliminar Cuenta",
                        description = "Borrar permanentemente tu cuenta",
                        onClick = { },
                        textColor = RedYachay
                    )
                }
            }

            // Sección de soporte
            item {
                SettingsSection(title = "Soporte y Ayuda") {
                    SettingsItem(
                        icon = Icons.Default.Help,
                        title = "Preguntas Frecuentes",
                        description = "Respuestas a dudas comunes",
                        onClick = { }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Email,
                        title = "Contactar Soporte",
                        description = "Enviar consulta al equipo técnico",
                        onClick = { }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Star,
                        title = "Calificar App",
                        description = "Déjanos tu opinión en la tienda",
                        onClick = { }
                    )
                }
            }

            // Información de la app
            item {
                SettingsSection(title = "Información") {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "Acerca de YACHAY HCO",
                        description = "Versión 1.0.0 • Build 2024.01",
                        onClick = { }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Policy,
                        title = "Términos y Condiciones",
                        description = "Políticas de uso y privacidad",
                        onClick = { }
                    )

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Code,
                        title = "Licencias Open Source",
                        description = "Bibliotecas de terceros utilizadas",
                        onClick = { }
                    )
                }
            }

            // Espacio adicional al final
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = BlueYachay,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    textColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onClick() }
            .padding(16.dp)
            .alpha(if (enabled) 1f else 0.6f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (textColor != Color.Unspecified) textColor else BlueYachay,
            modifier = Modifier.size(24.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = if (textColor != Color.Unspecified) textColor else Color.Black
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SwitchSettingsItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = BlueYachay,
            modifier = Modifier.size(24.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = GreenYachay,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
            )
        )
    }
}