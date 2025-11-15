package com.william.yachay_hco.repository

import android.net.Uri
import com.william.yachay_hco.network.ProfileService
import com.william.yachay_hco.utils.ImageUtils
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileService: ProfileService,
    private val imageUtils: ImageUtils
) {
    suspend fun saveProfile(name: String, email: String, interests: String, imageUri: Uri?): String {
        // Aquí procesarías la imagen y enviarías los datos al backend
        val imageUrl = imageUri?.let { imageUtils.uploadImage(it) }

        val profileData = mapOf(
            "name" to name,
            "email" to email,
            "interests" to interests,
            "imageUrl" to imageUrl
        )

        return profileService.saveProfile(profileData)
    }
}