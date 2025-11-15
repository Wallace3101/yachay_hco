package com.william.yachay_hco.repository

import android.content.Context
import android.util.Log
import com.william.yachay_hco.model.*
import com.william.yachay_hco.network.CulturalService
import com.william.yachay_hco.utils.SharedPreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CulturalRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val culturalService: CulturalService,
    private val sharedPreferenceManager: SharedPreferenceManager
) {

    companion object {
        private const val TAG = "CulturalRepository"
    }

    sealed class AnalysisError : Exception() {
        data class RateLimitError(
            override val message: String = "Debes esperar 30 segundos entre análisis"
        ) : AnalysisError()

        data class InvalidImageError(
            override val message: String = "La imagen proporcionada es inválida"
        ) : AnalysisError()

        data class NotCulturalError(
            override val message: String = "La imagen no pertenece a la cultura huanuqueña"
        ) : AnalysisError()

        data class NetworkError(
            override val message: String = "Error de conexión. Verifica tu internet"
        ) : AnalysisError()

        data class ServerError(
            override val message: String = "Error en el servidor. Intenta más tarde"
        ) : AnalysisError()

        data class UnauthorizedError(
            override val message: String = "Sesión expirada. Vuelve a iniciar sesión"
        ) : AnalysisError()

        data class ImageTooLargeError(
            override val message: String = "La imagen es demasiado grande (máx 10MB)"
        ) : AnalysisError()
    }

    private fun validateImage(imageBase64: String) {
        // Verificar que no esté vacía
        if (imageBase64.isBlank()) {
            throw AnalysisError.InvalidImageError("La imagen está vacía")
        }

        // Verificar tamaño (aproximado, base64 es ~33% más grande que original)
        val sizeInBytes = (imageBase64.length * 3) / 4
        val maxSizeInBytes = 10_000_000 // 10MB

        if (sizeInBytes > maxSizeInBytes) {
            throw AnalysisError.ImageTooLargeError(
                "La imagen es muy grande (${sizeInBytes / 1_000_000}MB). Máximo 10MB"
            )
        }

        // Opcional: verificar que sea base64 válido (verificación básica)
        if (!imageBase64.matches(Regex("^[A-Za-z0-9+/]*={0,2}$"))) {
            throw AnalysisError.InvalidImageError("Formato de imagen inválido")
        }
    }

    private fun getAuthToken(): String {
        val token = sharedPreferenceManager.getAuthToken()
        if (token.isNullOrBlank()) {
            throw AnalysisError.UnauthorizedError("No hay sesión activa")
        }
        return "Token $token" // Formato: "Token abc123..."
    }

    private fun parseError(response: ApiResponse<CulturalItem>): AnalysisError {
        val errorMessage = response.message ?: "Error desconocido"

        return when {
            // Errores específicos basados en el mensaje del backend
            errorMessage.contains("rate limit", ignoreCase = true) ||
                    errorMessage.contains("esperar", ignoreCase = true) -> {
                AnalysisError.RateLimitError(errorMessage)
            }

            errorMessage.contains("no pertenece", ignoreCase = true) ||
                    errorMessage.contains("no cultural", ignoreCase = true) ||
                    errorMessage.contains("confianza", ignoreCase = true) -> {
                AnalysisError.NotCulturalError(errorMessage)
            }

            errorMessage.contains("inválida", ignoreCase = true) ||
                    errorMessage.contains("formato", ignoreCase = true) -> {
                AnalysisError.InvalidImageError(errorMessage)
            }

            errorMessage.contains("sesión", ignoreCase = true) ||
                    errorMessage.contains("autenticación", ignoreCase = true) -> {
                AnalysisError.UnauthorizedError(errorMessage)
            }

            else -> {
                AnalysisError.ServerError(errorMessage)
            }
        }
    }

    private fun handleNetworkError(exception: Exception): AnalysisError {
        return when (exception) {
            is AnalysisError -> {
                // Si ya es un AnalysisError, retornarlo tal cual
                exception
            }

            is java.net.UnknownHostException -> {
                AnalysisError.NetworkError("Sin conexión a internet. Verifica tu red")
            }

            is java.net.SocketTimeoutException -> {
                AnalysisError.NetworkError("La conexión tardó demasiado. Intenta de nuevo")
            }

            is java.net.ConnectException -> {
                AnalysisError.NetworkError("No se pudo conectar al servidor")
            }

            is retrofit2.HttpException -> {
                when (exception.code()) {
                    401 -> AnalysisError.UnauthorizedError("Sesión expirada. Inicia sesión nuevamente")
                    429 -> AnalysisError.RateLimitError("Has excedido el límite de análisis. Espera un momento")
                    500, 502, 503, 504 -> AnalysisError.ServerError("El servidor está teniendo problemas. Intenta más tarde")
                    else -> AnalysisError.ServerError("Error del servidor (código ${exception.code()})")
                }
            }

            else -> {
                AnalysisError.ServerError("Error inesperado: ${exception.message}")
            }
        }
    }

    suspend fun analyzeCulturalImage(imageBase64: String): Result<CulturalItem> {
        return try {
            validateImage(imageBase64)

            val authToken = getAuthToken() // ✅ Aquí obtienes el token
            val request = CulturalAnalysisRequest(image = imageBase64)
            val response = culturalService.analyzeImage(authToken, request)

            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(parseError(response))
            }
        } catch (e: AnalysisError) {
            Log.e(TAG, "Error de análisis: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado: ${e.message}", e)
            Result.failure(handleNetworkError(e))
        }
    }

    suspend fun saveCulturalItem(item: CulturalItem): Result<CulturalItem> {
        return try {
            val token = sharedPreferenceManager.getAuthToken() ?: ""
            val response = culturalService.saveCulturalItem("Token $token", item)
            if (response.success && response.data != null) {  // ← Agregar && response.data != null
                Result.success(response.data)  // ← Ahora es no-nullable
            } else {
                Result.failure(Exception(response.message ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error guardando item cultural: ${e.message}", e)
            Result.failure(Exception("No se pudo guardar el elemento cultural: ${e.message}"))
        }
    }

    suspend fun getCulturalItems(category: CulturalCategory? = null): Result<List<CulturalItem>> {
        return try {
            val token = sharedPreferenceManager.getAuthToken() ?: ""
            val response = culturalService.getCulturalItems("Token $token", category?.name)
            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception("Error al obtener items"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo items culturales: ${e.message}", e)
            Result.failure(Exception("No se pudieron obtener los elementos culturales: ${e.message}"))
        }
    }

    suspend fun getCulturalItemDetail(itemId: Int): CulturalItem {
        val response = culturalService.getCulturalItemDetail("", itemId)

        if (response.success && response.data != null) {
            return response.data
        } else {
            throw Exception(response.message ?: "Error al obtener el detalle")
        }
    }

    suspend fun getUserCulturalItems(): Result<List<CulturalItem>> {
        return try {
            val token = sharedPreferenceManager.getAuthToken() ?: ""
            val response = culturalService.getMyCulturalItems("Token $token")

            if (response.success) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al obtener tus elementos culturales"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo tus items culturales: ${e.message}", e)
            Result.failure(Exception("No se pudieron obtener tus elementos culturales: ${e.message}"))
        }
    }

}