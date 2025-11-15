package com.william.yachay_hco.ui.screens.cultural_analysis_screen.partes

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

/**
 * Redimensiona un Bitmap manteniendo la relación de aspecto
 */
fun Bitmap.resizeBitmap(maxWidth: Int = 1024, maxHeight: Int = 1024): Bitmap {
    val width = this.width
    val height = this.height

    if (width <= maxWidth && height <= maxHeight) {
        return this
    }

    val scale = minOf(
        maxWidth.toFloat() / width.toFloat(),
        maxHeight.toFloat() / height.toFloat()
    )

    val newWidth = (width * scale).toInt()
    val newHeight = (height * scale).toInt()

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}

/**
 * Convierte Bitmap a Base64 con compresión y redimensionamiento automático
 */
fun bitmapToBase64(bitmap: Bitmap): String {
    val resizedBitmap = bitmap.resizeBitmap(maxWidth = 800, maxHeight = 800)

    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    val byteArray = outputStream.toByteArray()

    if (resizedBitmap != bitmap) {
        resizedBitmap.recycle()
    }

    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

/**
 * Carga un Bitmap desde Uri con redimensionamiento automático
 */
fun loadBitmapFromUri(context: Context, uri: Uri, maxWidth: Int = 1024, maxHeight: Int = 1024): Bitmap? {
    return try {
        val originalBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            android.graphics.ImageDecoder.decodeBitmap(android.graphics.ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

        originalBitmap.resizeBitmap(maxWidth, maxHeight)
    } catch (e: Exception) {
        android.util.Log.e("BitmapUtils", "Error loading bitmap: ${e.message}")
        null
    }
}

/**
 * Extensión para convertir Bitmap a ImageBitmap de Compose
 */
fun Bitmap.toImageBitmap(): ImageBitmap {
    return this.asImageBitmap()
}