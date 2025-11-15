package com.william.yachay_hco.network

import com.william.yachay_hco.model.User
import retrofit2.http.Body
import retrofit2.http.POST

// network/AuthService.kt
interface AuthService {
    @POST("api/auth/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): LoginResponse

    @POST("api/auth/google/")
    suspend fun loginWithGoogle(@Body request: GoogleAuthRequest): LoginResponse
}