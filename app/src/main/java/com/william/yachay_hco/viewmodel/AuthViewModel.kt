// viewmodel/AuthViewModel.kt
package com.william.yachay_hco.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.william.yachay_hco.model.User
import com.william.yachay_hco.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // ✅ Estado único de autenticación
    private val _authResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authResult: StateFlow<AuthResult> = _authResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            try {
                val user = authRepository.login(username, password)
                _authResult.value = AuthResult.Success(user)
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            try {
                val user = authRepository.register(username, email, password)
                _authResult.value = AuthResult.Success(user)
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error al registrar usuario")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            try {
                val user = authRepository.loginWithGoogle(idToken)
                _authResult.value = AuthResult.Success(user)
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Error al iniciar con Google")
            }
        }
    }

    fun logout() {
        authRepository.logout()
        _authResult.value = AuthResult.Logout
    }

    fun resetState() {
        _authResult.value = AuthResult.Idle
    }
}

// ✅ Estados claros y tipados
sealed class AuthResult {
    object Idle : AuthResult()           // Estado inicial (sin acción)
    object Loading : AuthResult()        // Procesando login/registro
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Logout : AuthResult()         // Sesión cerrada
}
