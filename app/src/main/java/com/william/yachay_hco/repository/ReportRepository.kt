package com.william.yachay_hco.repository

import android.content.Context
import android.util.Log
import com.william.yachay_hco.model.*
import com.william.yachay_hco.network.CulturalService
import com.william.yachay_hco.utils.SharedPreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReportRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val culturalService: CulturalService,
    private val sharedPreferenceManager: SharedPreferenceManager
) {

    companion object {
        private const val TAG = "ReportRepository"
    }

    sealed class ReportError : Exception() {
        data class UnauthorizedError(
            override val message: String = "Sesión expirada. Vuelve a iniciar sesión"
        ) : ReportError()

        data class NetworkError(
            override val message: String = "Error de conexión. Verifica tu internet"
        ) : ReportError()

        data class ServerError(
            override val message: String = "Error en el servidor. Intenta más tarde"
        ) : ReportError()

        data class NotFoundError(
            override val message: String = "Reporte no encontrado"
        ) : ReportError()

        data class ValidationError(
            override val message: String = "Datos inválidos"
        ) : ReportError()

        data class PermissionError(
            override val message: String = "No tienes permiso para realizar esta acción"
        ) : ReportError()
    }

    private fun getAuthToken(): String {
        val token = sharedPreferenceManager.getAuthToken()
        if (token.isNullOrBlank()) {
            throw ReportError.UnauthorizedError("No hay sesión activa")
        }
        return "Token $token"
    }

    private fun handleNetworkError(exception: Exception): ReportError {
        return when (exception) {
            is ReportError -> exception

            is java.net.UnknownHostException -> {
                ReportError.NetworkError("Sin conexión a internet. Verifica tu red")
            }

            is java.net.SocketTimeoutException -> {
                ReportError.NetworkError("La conexión tardó demasiado. Intenta de nuevo")
            }

            is java.net.ConnectException -> {
                ReportError.NetworkError("No se pudo conectar al servidor")
            }

            is retrofit2.HttpException -> {
                when (exception.code()) {
                    401 -> ReportError.UnauthorizedError("Sesión expirada. Inicia sesión nuevamente")
                    403 -> ReportError.PermissionError("No tienes permiso para esta acción")
                    404 -> ReportError.NotFoundError("Reporte no encontrado")
                    400 -> ReportError.ValidationError("Datos inválidos")
                    500, 502, 503, 504 -> ReportError.ServerError("El servidor está teniendo problemas")
                    else -> ReportError.ServerError("Error del servidor (código ${exception.code()})")
                }
            }

            else -> {
                ReportError.ServerError("Error inesperado: ${exception.message}")
            }
        }
    }

    suspend fun createReport(request: CreateReportRequest): Result<Report> {
        return try {
            val authToken = getAuthToken()
            val response = culturalService.createReport(authToken, request)

            if (response.success && response.data != null) {
                Log.d(TAG, "Reporte creado exitosamente: ${response.data.id}")
                Result.success(response.data)
            } else {
                Result.failure(ReportError.ServerError(response.message ?: "Error al crear reporte"))
            }
        } catch (e: ReportError) {
            Log.e(TAG, "Error de reporte: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado: ${e.message}", e)
            Result.failure(handleNetworkError(e))
        }
    }

    suspend fun getUserReports(): Result<List<Report>> {
        return try {
            val authToken = getAuthToken()
            val response = culturalService.getUserReports(authToken)

            if (response.success) {
                Log.d(TAG, "Reportes del usuario obtenidos: ${response.data.size}")
                Result.success(response.data)
            } else {
                Result.failure(ReportError.ServerError(response.message ?: "Error al obtener reportes"))
            }
        } catch (e: ReportError) {
            Log.e(TAG, "Error de reporte: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado: ${e.message}", e)
            Result.failure(handleNetworkError(e))
        }
    }

    suspend fun getAllReports(status: String? = null): Result<List<Report>> {
        return try {
            val authToken = getAuthToken()
            val response = culturalService.getAllReports(authToken, status)

            if (response.success) {
                Log.d(TAG, "Todos los reportes obtenidos: ${response.data.size}")
                Result.success(response.data)
            } else {
                Result.failure(ReportError.ServerError(response.message ?: "Error al obtener reportes"))
            }
        } catch (e: ReportError) {
            Log.e(TAG, "Error de reporte: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado: ${e.message}", e)
            Result.failure(handleNetworkError(e))
        }
    }

    suspend fun getReportDetail(reportId: Int): Result<Report> {
        return try {
            val authToken = getAuthToken()
            val response = culturalService.getReportDetail(authToken, reportId)

            if (response.success && response.data != null) {
                Log.d(TAG, "Detalle del reporte obtenido: ${response.data.id}")
                Result.success(response.data)
            } else {
                Result.failure(ReportError.NotFoundError(response.message ?: "Reporte no encontrado"))
            }
        } catch (e: ReportError) {
            Log.e(TAG, "Error de reporte: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado: ${e.message}", e)
            Result.failure(handleNetworkError(e))
        }
    }

    suspend fun reviewReport(reportId: Int, action: String, adminNotes: String? = null): Result<ReviewReportData> {
        return try {
            val authToken = getAuthToken()
            val request = ReviewReportRequest(action = action, admin_notes = adminNotes)
            val response = culturalService.reviewReport(authToken, reportId, request)

            if (response.success && response.data != null) {
                Log.d(TAG, "Reporte revisado exitosamente: $action")
                Result.success(response.data)
            } else {
                Result.failure(ReportError.ServerError(response.message ?: "Error al revisar reporte"))
            }
        } catch (e: ReportError) {
            Log.e(TAG, "Error de reporte: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado: ${e.message}", e)
            Result.failure(handleNetworkError(e))
        }
    }
}