package com.william.yachay_hco.view.compose.auth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.william.yachay_hco.R
import com.william.yachay_hco.ui.theme.*
import com.william.yachay_hco.viewmodel.AuthResult
import com.william.yachay_hco.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToHome: (Boolean) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessScreen by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    val context = LocalContext.current

    val googleSignInClient = remember {
        val webClientId = try {
            context.getString(R.string.default_web_client_id)
        } catch (e: Exception) {
            null
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).apply {
            if (webClientId != null) {
                requestIdToken(webClientId)
            }
            requestEmail()
            requestProfile()
        }.build()

        GoogleSignIn.getClient(context, gso)
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val idToken = account.idToken
                    userName = account.displayName ?: "Usuario" // Guardar nombre para mostrar
                    if (idToken != null) {
                        isLoading = true
                        viewModel.signInWithGoogle(idToken)
                    } else {
                        Toast.makeText(context, "Error: No se recibió token de Google", Toast.LENGTH_LONG).show()
                    }
                } catch (e: ApiException) {
                    val errorCode = e.statusCode
                    val errorMessage = when (errorCode) {
                        GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> "Inicio de sesión cancelado"
                        GoogleSignInStatusCodes.SIGN_IN_FAILED -> "Error en inicio de sesión"
                        GoogleSignInStatusCodes.NETWORK_ERROR -> "Error de red"
                        GoogleSignInStatusCodes.DEVELOPER_ERROR -> "Error de configuración (verifica Web Client ID)"
                        GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS -> "Inicio de sesión ya en progreso"
                        else -> "Error desconocido: $errorCode"
                    }
                    Toast.makeText(context, "Error Google: $errorMessage", Toast.LENGTH_LONG).show()
                    isLoading = false
                }
            }
            Activity.RESULT_CANCELED -> {
                Toast.makeText(context, "Inicio con Google cancelado por el usuario", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
            else -> {
                Toast.makeText(context, "Error inesperado en Google Sign-In", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        }
    }

    fun startGoogleSignIn() {
        try {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error al iniciar Google Sign-In: ${e.message}", Toast.LENGTH_LONG).show()
            isLoading = false
        }
    }

    LaunchedEffect(viewModel.authResult) {
        viewModel.authResult.collect { result ->
            when (result) {
                is AuthResult.Success -> {
                    isLoading = false
                    // Mostrar pantalla de éxito antes de navegar
                    showSuccessScreen = true
                    // Esperar 2 segundos y luego navegar
                    delay(2000)
                    navigateToHome(result.user.is_staff || result.user.is_superuser)
                }
                is AuthResult.Error -> {
                    isLoading = false
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    // Mostrar pantalla de éxito o pantalla de login normal
    if (showSuccessScreen) {
        LoginSuccessScreen(userName = userName)
    } else {
        LoginContent(
            isLoading = isLoading,
            onGoogleSignIn = { startGoogleSignIn() }
        )
    }
}

@Composable
fun LoginContent(
    isLoading: Boolean,
    onGoogleSignIn: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // IMAGEN DE FONDO
        Image(
            painter = painterResource(R.drawable.background_login),
            contentDescription = "Fondo de pantalla de login",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay sutil para mejorar legibilidad del contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.05f)
                        )
                    )
                )
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo y título con diseño moderno
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(BlueYachay, GreenYachay)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_yachay_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "YACHAY HCO",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Conecta con nuestro patrimonio cultural",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Tarjeta principal con diseño limpio - SOLO GOOGLE
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bienvenido",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueYachay
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Inicia sesión para explorar nuestro patrimonio cultural",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // ÚNICO BOTÓN - GOOGLE
                    Button(
                        onClick = onGoogleSignIn,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.5.dp, Color(0xFFE0E0E0)),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading,
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = BlueYachay,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.ic_google),
                                contentDescription = "Google",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Continuar con Google",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Texto informativo
                    Text(
                        text = "Esta es la única forma de acceder a la aplicación",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer sutil
            Text(
                text = "Al continuar, aceptas nuestros términos y condiciones",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
fun LoginSuccessScreen(userName: String) {
    var scale by remember { mutableStateOf(0.8f) }
    var opacity by remember { mutableStateOf(0f) }

    // Animación de entrada
    LaunchedEffect(Unit) {
        scale = 1f
        opacity = 1f
    }

    val scaleAnimation by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scaleAnimation"
    )

    val opacityAnimation by animateFloatAsState(
        targetValue = opacity,
        animationSpec = tween(durationMillis = 800),
        label = "opacityAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BlueYachay, GreenYachay)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .scale(scaleAnimation)
                .alpha(opacityAnimation),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono de check animado
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Login exitoso",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scaleAnimation),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "¡Bienvenido!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (userName.isNotEmpty()) "Hola, $userName" else "Inicio de sesión exitoso",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu sesión se ha iniciado correctamente",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Loading indicator mientras espera para navegar
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                color = Color.White,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Redirigiendo...",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}