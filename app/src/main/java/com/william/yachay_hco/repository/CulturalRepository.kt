package com.william.yachay_hco.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.william.yachay_hco.R
import com.william.yachay_hco.model.*
import com.william.yachay_hco.network.OpenAIService
import com.william.yachay_hco.network.CulturalService
import com.william.yachay_hco.utils.SharedPreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random

class CulturalRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val openAIService: OpenAIService,
    private val culturalService: CulturalService,
    private val sharedPreferenceManager: SharedPreferenceManager
) {

    companion object {
        private const val TAG = "CulturalRepository"
    }

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

    suspend fun analyzeCulturalImage(imageBase64: String): CulturalItem? {
        try {
            Log.d(TAG, "Iniciando análisis de imagen cultural")

            // 1. Obtener API key interna
            val apiKey = getInternalApiKey()

            // 2. Verificar límite de análisis
            if (!sharedPreferenceManager.canPerformAnalysis()) {
                Log.w(TAG, "Análisis bloqueado por límite de tiempo")
                throw IllegalStateException("Debes esperar 30 segundos entre análisis")
            }

            // 3. Validar imagen base64
            if (imageBase64.isBlank()) {
                Log.e(TAG, "Imagen base64 está vacía")
                throw IllegalArgumentException("Imagen inválida")
            }

            Log.d(TAG, "Creando request para OpenAI")
            val prompt = createHuanucoCulturalPrompt()

            val request = OpenAIVisionRequest(
                messages = listOf(
                    OpenAIMessage(
                        role = "user",
                        content = listOf(
                            OpenAIContent(
                                type = "text",
                                text = prompt
                            ),
                            OpenAIContent(
                                type = "image_url",
                                image_url = OpenAIImageUrl(url = "data:image/jpeg;base64,$imageBase64")
                            )
                        )
                    )
                )
            )

            Log.d(TAG, "Enviando request a OpenAI")
            val response = openAIService.analyzeImage(
                authToken = "Bearer $apiKey",
                request = request
            )

            Log.d(TAG, "Respuesta recibida de OpenAI: ${response.choices.firstOrNull()?.message?.content}")

            // 4. Verificar que hay respuesta
            if (response.choices.isEmpty()) {
                Log.e(TAG, "OpenAI no retornó ninguna respuesta")
                throw RuntimeException("OpenAI no pudo analizar la imagen")
            }

            val content = response.choices.first().message.content
            if (content.isBlank()) {
                Log.e(TAG, "Contenido de respuesta está vacío")
                throw RuntimeException("OpenAI retornó respuesta vacía")
            }

            // 5. Parsear la respuesta JSON estructurada
            Log.d(TAG, "Parseando respuesta JSON")
            val analysisResult = parseOpenAIResponse(content)

            // 6. Actualizar tiempo del último análisis
            sharedPreferenceManager.saveLastAnalysisTime(System.currentTimeMillis())
            sharedPreferenceManager.incrementAnalysisCount()

            // 7. Convertir a CulturalItem
            val culturalItem = CulturalItem(
                imagen = imageBase64,
                titulo = analysisResult.titulo,
                categoria = mapStringToCategory(analysisResult.categoria),
                confianza = analysisResult.confianza,
                descripcion = analysisResult.descripcion,
                contexto_cultural = analysisResult.contexto_cultural,
                periodo_historico = analysisResult.periodo_historico,
                ubicacion = analysisResult.ubicacion,
                significado = analysisResult.significado
            )

            Log.d(TAG, "Análisis completado exitosamente: ${culturalItem.titulo}")
            return culturalItem

        } catch (e: Exception) {
            Log.e(TAG, "Error en análisis de imagen: ${e.message}", e)
            throw when (e) {
                is IllegalStateException -> e
                is IllegalArgumentException -> e
                else -> RuntimeException("Error al comunicarse con OpenAI: ${e.message}")
            }
        }
    }

    private fun createHuanucoCulturalPrompt(): String {
        return """
            Analiza esta imagen y determina si representa algún elemento cultural de Huánuco, Perú. 
            Categoriza entre: Gastronomía, Patrimonio Arqueológico, Flora Medicinal, o Leyendas y Tradiciones.
            
            Responde ÚNICAMENTE con un JSON válido en este formato exacto:
            {
                "titulo": "Nombre específico del elemento",
                "categoria": "Una de las 4 categorías mencionadas",
                "confianza": 0.85,
                "descripcion": "Descripción detallada del elemento cultural",
                "contexto_cultural": "Importancia cultural específica para Huánuco",
                "periodo_historico": "Época o periodo histórico relevante",
                "ubicacion": "Ubicación específica en Huánuco donde se encuentra/practica",
                "significado": "Significado cultural y simbólico para la comunidad huanuqueña"
            }
            
            Si no puedes identificar el elemento como parte de la cultura huanuqueña, usa confianza menor a 0.3 y especifica por qué no es reconocible como elemento cultural de Huánuco.
        """.trimIndent()
    }

    private fun parseOpenAIResponse(content: String): CulturalAnalysisResult {
        return try {
            Log.d(TAG, "Intentando parsear JSON: $content")
            val cleanContent = cleanJsonResponse(content)
            Log.d(TAG, "JSON limpio: $cleanContent")

            val gson = Gson()
            val result = gson.fromJson(cleanContent, CulturalAnalysisResult::class.java)

            if (result.titulo.isBlank()) {
                throw IllegalStateException("Título vacío en respuesta de OpenAI")
            }

            Log.d(TAG, "JSON parseado exitosamente")
            return result

        } catch (e: Exception) {
            Log.e(TAG, "Error parseando JSON de OpenAI: ${e.message}", e)
            Log.e(TAG, "Contenido problemático: $content")
            throw RuntimeException("OpenAI retornó un formato inválido: ${e.message}")
        }
    }

    private fun cleanJsonResponse(content: String): String {
        val jsonStart = content.indexOf("{")
        val jsonEnd = content.lastIndexOf("}") + 1

        return if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
            content.substring(jsonStart, jsonEnd)
        } else {
            content.trim()
        }
    }

    private fun mapStringToCategory(categoryString: String): CulturalCategory {
        return when (categoryString.lowercase().trim()) {
            "gastronomía", "gastronomia" -> CulturalCategory.GASTRONOMIA
            "patrimonio arqueológico", "patrimonio arqueologico" -> CulturalCategory.PATRIMONIO_ARQUEOLOGICO
            "flora medicinal" -> CulturalCategory.FLORA_MEDICINAL
            "leyendas y tradiciones" -> CulturalCategory.LEYENDAS_Y_TRADICIONES
            else -> {
                Log.w(TAG, "Categoría no reconocida: $categoryString, usando GASTRONOMIA por defecto")
                CulturalCategory.GASTRONOMIA
            }
        }
    }

    /**
     * Obtiene la API key interna desde los recursos de la aplicación
     */
    private fun getInternalApiKey(): String {
        return context.getString(R.string.openai_api_key)
    }

    suspend fun saveCulturalItem(item: CulturalItem): CulturalItem {
        return try {
            culturalService.saveCulturalItem(item)
        } catch (e: Exception) {
            item.copy(id = Random.nextInt(1000))
        }
    }

    suspend fun getCulturalItems(category: CulturalCategory? = null): List<CulturalItem> {
        return try {
            culturalService.getCulturalItems(category?.name)
        } catch (e: Exception) {
            createMockCulturalItems()
        }
    }

    private fun createMockCulturalItems(): List<CulturalItem> {
        return listOf(
            CulturalItem(
                id = 1,
                titulo = "Pachamanca Huanuqueña",
                categoria = CulturalCategory.GASTRONOMIA,
                confianza = 0.95f,
                descripcion = "Plato tradicional que se cocina bajo tierra usando piedras calientes",
                contexto_cultural = "Ritual gastronómico ancestral que fortalece vínculos comunitarios",
                periodo_historico = "Época preinca - actualidad",
                ubicacion = "Toda la región de Huánuco",
                significado = "Símbolo de unión familiar y respeto a la Pachamama",
                imagen = "",
            ),
            CulturalItem(
                id = 2,
                titulo = "Templo de las Manos Cruzadas - Kotosh",
                categoria = CulturalCategory.PATRIMONIO_ARQUEOLOGICO,
                confianza = 0.98f,
                descripcion = "Uno de los templos más antiguos de América",
                contexto_cultural = "Evidencia de las primeras tradiciones arquitectónicas y religiosas",
                periodo_historico = "2000 a.C. - 500 a.C.",
                ubicacion = "Distrito de Kotosh, Huánuco",
                significado = "Origen de la tradición arquitectónica ceremonial andina",
                imagen = "",
            )
        )
    }
}