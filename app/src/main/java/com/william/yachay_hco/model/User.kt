package com.william.yachay_hco.model

import kotlinx.serialization.SerialName

data class User(
    val id: Int,                    // Django usa auto-incrementing integer
    val username: String,
    val email: String,
    val firstName: String? = null,  // De AbstractUser
    val lastName: String? = null,   // De AbstractUser
    val createdAt: String? = null,  // ISO string desde Django
    val updatedAt: String? = null,  // ISO string desde Django

    // ← nuevos
    val is_staff: Boolean = false,
    val is_superuser: Boolean = false
)