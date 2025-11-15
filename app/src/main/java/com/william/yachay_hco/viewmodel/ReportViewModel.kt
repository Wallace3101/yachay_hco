package com.william.yachay_hco.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.william.yachay_hco.model.*
import com.william.yachay_hco.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {

    companion object {
        private const val TAG = "ReportViewModel"
    }

    // Estado para la creación de reportes
    private val _createReportState = MutableStateFlow<UiState<Report>>(UiState.Idle)
    val createReportState: StateFlow<UiState<Report>> = _createReportState.asStateFlow()

    // Estado para los reportes del usuario
    private val _userReportsState = MutableStateFlow<UiState<List<Report>>>(UiState.Idle)
    val userReportsState: StateFlow<UiState<List<Report>>> = _userReportsState.asStateFlow()

    // Estado para todos los reportes (admin)
    private val _allReportsState = MutableStateFlow<UiState<List<Report>>>(UiState.Idle)
    val allReportsState: StateFlow<UiState<List<Report>>> = _allReportsState.asStateFlow()

    // Estado para el detalle de un reporte
    private val _reportDetailState = MutableStateFlow<UiState<Report>>(UiState.Idle)
    val reportDetailState: StateFlow<UiState<Report>> = _reportDetailState.asStateFlow()

    // Estado para la revisión de reportes (admin)
    private val _reviewReportState = MutableStateFlow<UiState<ReviewReportData>>(UiState.Idle)
    val reviewReportState: StateFlow<UiState<ReviewReportData>> = _reviewReportState.asStateFlow()

    /**
     * Estados de UI genéricos
     */
    sealed class UiState<out T> {
        object Idle : UiState<Nothing>()
        object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val message: String) : UiState<Nothing>()
    }

    /**
     * Crear un nuevo reporte
     */
    fun createReport(request: CreateReportRequest) {
        viewModelScope.launch {
            _createReportState.value = UiState.Loading

            val result = reportRepository.createReport(request)

            _createReportState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                val error = result.exceptionOrNull()
                UiState.Error(error?.message ?: "Error desconocido al crear el reporte")
            }
        }
    }

    /**
     * Obtener los reportes del usuario autenticado
     */
    fun getUserReports() {
        viewModelScope.launch {
            _userReportsState.value = UiState.Loading

            val result = reportRepository.getUserReports()

            _userReportsState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                val error = result.exceptionOrNull()
                UiState.Error(error?.message ?: "Error al obtener tus reportes")
            }
        }
    }

    /**
     * Obtener todos los reportes (solo para administradores)
     * @param status Filtro opcional por estado (PENDIENTE, APROBADO, RECHAZADO)
     */
    fun getAllReports(status: String? = null) {
        viewModelScope.launch {
            _allReportsState.value = UiState.Loading

            val result = reportRepository.getAllReports(status)

            _allReportsState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                val error = result.exceptionOrNull()
                UiState.Error(error?.message ?: "Error al obtener los reportes")
            }
        }
    }

    /**
     * Obtener el detalle de un reporte específico
     */
    fun getReportDetail(reportId: Int) {
        viewModelScope.launch {
            _reportDetailState.value = UiState.Loading

            val result = reportRepository.getReportDetail(reportId)

            _reportDetailState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                val error = result.exceptionOrNull()
                UiState.Error(error?.message ?: "Error al obtener el detalle del reporte")
            }
        }
    }

    /**
     * Revisar un reporte (solo para administradores)
     * @param reportId ID del reporte a revisar
     * @param action "approve" o "reject"
     * @param adminNotes Notas opcionales del administrador
     */
    fun reviewReport(reportId: Int, action: String, adminNotes: String? = null) {
        viewModelScope.launch {
            _reviewReportState.value = UiState.Loading

            val result = reportRepository.reviewReport(reportId, action, adminNotes)

            _reviewReportState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull()!!)
            } else {
                val error = result.exceptionOrNull()
                UiState.Error(error?.message ?: "Error al revisar el reporte")
            }
        }
    }

    /**
     * Aprobar un reporte (atajo para reviewReport)
     */
    fun approveReport(reportId: Int, adminNotes: String? = null) {
        reviewReport(reportId, "approve", adminNotes)
    }

    /**
     * Rechazar un reporte (atajo para reviewReport)
     */
    fun rejectReport(reportId: Int, adminNotes: String? = null) {
        reviewReport(reportId, "reject", adminNotes)
    }

    /**
     * Resetear el estado de creación de reporte
     */
    fun resetCreateReportState() {
        _createReportState.value = UiState.Idle
    }

    /**
     * Resetear el estado de revisión de reporte
     */
    fun resetReviewReportState() {
        _reviewReportState.value = UiState.Idle
    }

    /**
     * Resetear el estado de detalle de reporte
     */
    fun resetReportDetailState() {
        _reportDetailState.value = UiState.Idle
    }

    /**
     * Resetear el estado de reportes de usuario
     */
    fun resetUserReportsState() {
        _userReportsState.value = UiState.Idle
    }

    /**
     * Resetear el estado de todos los reportes
     */
    fun resetAllReportsState() {
        _allReportsState.value = UiState.Idle
    }
}