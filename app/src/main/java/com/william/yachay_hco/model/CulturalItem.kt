package com.william.yachay_hco.model


data class CulturalItem(
    val id: Int? = null,
    val imagen: String?,
    val imagen_base64: String?,
    val titulo: String,
    val categoria: CulturalCategory,
    val confianza: Float,                  // 0.0 a 1.0
    val descripcion: String,
    val contexto_cultural: String,
    val periodo_historico: String,
    val ubicacion: String,
    val significado: String,
    val created_at: String? = null,
    val updated_at: String? = null,
    val created_by: Int? = null,
)

enum class CulturalCategory(val displayName: String) {
    GASTRONOMIA("Gastronomía"),
    PATRIMONIO_ARQUEOLOGICO("Patrimonio Arqueológico"),
    FLORA_MEDICINAL("Flora Medicinal"),
    LEYENDAS_Y_TRADICIONES("Leyendas y Tradiciones"),
    FESTIVIDADES("Festividades"),
    DANZA("Danza"),
    MUSICA("Música"),
    VESTIMENTA("Vestimenta"),
    ARTE_POPULAR("Arte Popular"),
    NATURALEZA_CULTURAL("Naturaleza/Cultural"),
    OTRO("Otro")
}

// Response genérica del backend
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T
)

data class CulturalAnalysisRequest(
    val image: String, // Base64 encoded image
    val location: String = "Huánuco, Perú"
)


// Agrega estas data classes en el mismo archivo o en un archivo de modelos
data class CulturalItemResponse(
    val success: Boolean,
    val message: String?,
    val data: CulturalItem?
)

data class CulturalItemsResponse(
    val success: Boolean,
    val message: String? = null,
    val data: List<CulturalItem>
)