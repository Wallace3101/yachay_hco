package com.william.yachay_hco.model

data class Report(
    val id: Int? = null,
    val report_type: ReportType,
    val motivo: String,

    // Datos del elemento cultural reportado/sugerido
    val titulo: String,
    val categoria: CulturalCategory,
    val descripcion: String,
    val contexto_cultural: String,
    val periodo_historico: String,
    val ubicacion: String,
    val significado: String,
    val confianza: Float = 0.85f,

    // Imagen
    val imagen: String? = null,  // URL de la imagen

    // Estado y auditoría
    val status: ReportStatus = ReportStatus.PENDIENTE,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val reportedByEmail: String? = null,
    val reviewedByEmail: String? = null,
    val reviewed_at: String? = null,
    val admin_notes: String? = null,
    val created_cultural_item: Int? = null  // ID del elemento cultural creado
)

enum class ReportType(val displayName: String, val apiValue: String) {
    CORRECCION("Corrección de análisis", "CORRECCION"),
    NUEVO_ELEMENTO("Nuevo elemento cultural", "NUEVO_ELEMENTO")
}

enum class ReportStatus(val displayName: String, val apiValue: String) {
    PENDIENTE("Pendiente", "PENDIENTE"),
    APROBADO("Aprobado", "APROBADO"),
    RECHAZADO("Rechazado", "RECHAZADO")
}

// Request para crear un reporte
data class CreateReportRequest(
    val report_type: String,  // "CORRECCION" o "NUEVO_ELEMENTO"
    val motivo: String,
    val titulo: String,
    val categoria: String,  // Nombre del enum (ej: "GASTRONOMIA")
    val descripcion: String,
    val contexto_cultural: String,
    val periodo_historico: String,
    val ubicacion: String,
    val significado: String,
    val confianza: Float = 0.85f,
    val imagen_base64: String? = null  // Imagen en base64 (opcional)
)

// Response para un solo reporte
data class ReportResponse(
    val success: Boolean,
    val message: String?,
    val data: Report?
)

// Response para lista de reportes
data class ReportsResponse(
    val success: Boolean,
    val message: String? = null,
    val data: List<Report>,
    val count: Int? = null
)

// Request para revisar un reporte (admin)
data class ReviewReportRequest(
    val action: String,  // "approve" o "reject"
    val admin_notes: String? = null
)

// Response al revisar un reporte
data class ReviewReportResponse(
    val success: Boolean,
    val message: String?,
    val data: ReviewReportData?
)

data class ReviewReportData(
    val report: Report,
    val cultural_item: CulturalItem? = null,
    val added_to_json: Boolean? = null
)