package com.william.yachay_hco.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageUtils (private val context: Context) {

    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("images")
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    suspend fun uploadImage(imageUri: Uri): String {
        // Aquí implementarías la lógica para subir la imagen a tu servidor
        // y devolver la URL de la imagen subida
        return "https://ejemplo.com/imagenes/perfil.jpg"
    }

    fun getImageUriFromFile(file: File): Uri {
        return Uri.fromFile(file)
    }
}