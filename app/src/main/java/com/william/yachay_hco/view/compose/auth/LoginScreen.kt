package com.william.yachay_hco.view.compose.auth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.william.yachay_hco.R
import com.william.yachay_hco.ui.theme.BlueYachay
import com.william.yachay_hco.ui.theme.CreamYachay
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.viewmodel.AuthResult
import com.william.yachay_hco.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToForgotPassword: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // 🔥 SOLUCIÓN 1: Cerrar sesión previa y forzar selección de cuenta
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

    // 🔥 SOLUCIÓN 2: Launcher mejorado con mejor manejo
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    val idToken = account.idToken
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

    // 🔥 SOLUCIÓN 3: Función para iniciar Google Sign-In con limpieza previa
    fun startGoogleSignIn() {
        try {
            // Cerrar sesión previa para forzar selección de cuenta
            googleSignInClient.signOut().addOnCompleteListener {
                // Crear intent de sign-in
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error al iniciar Google Sign-In: ${e.message}", Toast.LENGTH_LONG).show()
            isLoading = false
        }
    }

    // Escuchar los resultados de autenticación
    LaunchedEffect(viewModel.authResult) {
        viewModel.authResult.collect { result ->
            isLoading = false
            when (result) {
                is AuthResult.Success -> navigateToHome()
                is AuthResult.Error -> Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BlueYachay, GreenYachay)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo y título
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_yachay_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bienvenido a YACHAY HCO",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Te extrañamos, ¡vuelve a conectarte con nuestro patrimonio cultural!",
                color = CreamYachay,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Iniciar Sesión",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueYachay
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Usuario
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = "Usuario")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = "Contraseña")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Olvidé contraseña
                    TextButton(
                        onClick = navigateToForgotPassword,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("¿Olvidaste tu contraseña?")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón de inicio de sesión
                    Button(
                        onClick = {
                            isLoading = true
                            viewModel.login(username, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlueYachay,
                            contentColor = Color.White
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Iniciar Sesión")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Separador
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f))
                        Text(
                            text = "o",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color.Gray
                        )
                        Divider(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // 🔥 BOTÓN DE GOOGLE MEJORADO
                    OutlinedButton(
                        onClick = {
                            if (!isLoading) {
                                isLoading = true
                                startGoogleSignIn()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        ),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.Gray
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.ic_google),
                                contentDescription = "Google",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Iniciar con Google")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Registro
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("¿No tienes cuenta? ")
                        TextButton(onClick = navigateToRegister) {
                            Text("Crear una")
                        }
                    }
                }
            }
        }
    }
}