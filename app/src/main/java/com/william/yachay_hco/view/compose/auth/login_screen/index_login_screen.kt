package com.william.yachay_hco.view.compose.auth.login_screen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.william.yachay_hco.R
import com.william.yachay_hco.view.compose.auth.login_screen.partes.LoginContent
import com.william.yachay_hco.view.compose.auth.login_screen.partes.LoginSuccessScreen
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
                    userName = account.displayName ?: "Usuario"
                    if (idToken != null) {
                        isLoading = true
                        viewModel.signInWithGoogle(idToken)
                    } else {
                        Toast.makeText(
                            context,
                            "Error: No se recibió token de Google",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: ApiException) {
                    val errorCode = e.statusCode
                    val errorMessage = when (errorCode) {
                        GoogleSignInStatusCodes.SIGN_IN_CANCELLED ->
                            "Inicio de sesión cancelado"
                        GoogleSignInStatusCodes.SIGN_IN_FAILED ->
                            "Error en inicio de sesión"
                        GoogleSignInStatusCodes.NETWORK_ERROR ->
                            "Error de red"
                        GoogleSignInStatusCodes.DEVELOPER_ERROR ->
                            "Error de configuración (verifica Web Client ID)"
                        GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS ->
                            "Inicio de sesión ya en progreso"
                        else -> "Error desconocido: $errorCode"
                    }
                    Toast.makeText(
                        context,
                        "Error Google: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                    isLoading = false
                }
            }

            Activity.RESULT_CANCELED -> {
                Toast.makeText(
                    context,
                    "Inicio con Google cancelado por el usuario",
                    Toast.LENGTH_SHORT
                ).show()
                isLoading = false
            }

            else -> {
                Toast.makeText(
                    context,
                    "Error inesperado en Google Sign-In",
                    Toast.LENGTH_SHORT
                ).show()
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
            Toast.makeText(
                context,
                "Error al iniciar Google Sign-In: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            isLoading = false
        }
    }

    LaunchedEffect(viewModel.authResult) {
        viewModel.authResult.collect { result ->
            when (result) {
                is AuthResult.Success -> {
                    isLoading = false
                    showSuccessScreen = true
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

    if (showSuccessScreen) {
        LoginSuccessScreen(userName = userName)
    } else {
        LoginContent(
            isLoading = isLoading,
            onGoogleSignIn = { startGoogleSignIn() }
        )
    }
}
