package com.william.yachay_hco.network

import com.william.yachay_hco.model.User

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class GoogleAuthRequest(
    val idToken: String
)

data class LoginResponse(
    val token: String,
    val user: User
)