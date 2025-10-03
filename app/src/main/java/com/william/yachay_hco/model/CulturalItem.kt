package com.william.yachay_hco.model

data class CulturalItem(
    val id: Int? = null,
    val imagen: String?,                    // URL o base64 de la imagen
    val titulo: String,
    val categoria: CulturalCategory,
    val confianza: Float,                  // 0.0 a 1.0
    val descripcion: String,
    val contexto_cultural: String,
    val periodo_historico: String,
    val ubicacion: String,
    val significado: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

enum class CulturalCategory(val displayName: String) {
    GASTRONOMIA("Gastronomía"),
    PATRIMONIO_ARQUEOLOGICO("Patrimonio Arqueológico"),
    FLORA_MEDICINAL("Flora Medicinal"),
    LEYENDAS_Y_TRADICIONES("Leyendas y Tradiciones")
}

// Para la respuesta de OpenAI Vision
data class OpenAIVisionRequest(
    val model: String = "gpt-4.1",
    val messages: List<OpenAIMessage>,
    val max_tokens: Int = 1000
)

data class OpenAIMessage(
    val role: String,
    val content: List<OpenAIContent>
)

data class OpenAIContent(
    val type: String,
    val text: String? = null,
    val image_url: OpenAIImageUrl? = null
)

data class OpenAIImageUrl(
    val url: String
)

data class OpenAIVisionResponse(
    val choices: List<OpenAIChoice>
)

data class OpenAIChoice(
    val message: OpenAIResponseMessage
)

data class OpenAIResponseMessage(
    val content: String
)

// Respuesta estructurada parseada
data class CulturalAnalysisResult(
    val titulo: String,
    val categoria: String,
    val confianza: Float,
    val descripcion: String,
    val contexto_cultural: String,
    val periodo_historico: String,
    val ubicacion: String,
    val significado: String
)