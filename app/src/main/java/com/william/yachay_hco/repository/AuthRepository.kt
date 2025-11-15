package com.william.yachay_hco.repository

import com.william.yachay_hco.model.User
import com.william.yachay_hco.network.AuthService
import com.william.yachay_hco.network.GoogleAuthRequest
import com.william.yachay_hco.network.LoginRequest
import com.william.yachay_hco.network.LoginResponse
import com.william.yachay_hco.network.RegisterRequest
import com.william.yachay_hco.utils.SharedPreferenceManager
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val sharedPreferenceManager: SharedPreferenceManager
) {

    suspend fun login(username: String, password: String): User {
        // Cuando esté lista tu API Django, reemplaza con
        val request = LoginRequest(username, password)
        val response: LoginResponse = authService.login(request)
        sharedPreferenceManager.saveAuthToken(response.token)
        sharedPreferenceManager.saveUser(response.user)
        return response.user
    }

    suspend fun register(username: String, email: String, password: String): User {
        val request = RegisterRequest(username, email, password)
        val response: LoginResponse = authService.register(request)

        // Guardar el token de autenticación
        sharedPreferenceManager.saveAuthToken(response.token)
        sharedPreferenceManager.saveUser(response.user)

        return response.user
    }

    suspend fun loginWithGoogle(idToken: String): User {
        // Crear el request con el idToken real
        val request = GoogleAuthRequest(idToken)

        // Llamada a la API con Retrofit
        val response: LoginResponse = authService.loginWithGoogle(request)

        // Guardar el token de autenticación y el usuario
        sharedPreferenceManager.saveAuthToken(response.token)
        sharedPreferenceManager.saveUser(response.user)

        return response.user
    }

    fun logout() {
        sharedPreferenceManager.clearAuthData()
    }

    fun getCurrentUser(): User? {
        return sharedPreferenceManager.getUser()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferenceManager.getAuthToken() != null
    }
}