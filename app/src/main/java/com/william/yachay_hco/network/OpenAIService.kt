package com.william.yachay_hco.network

import com.william.yachay_hco.model.CulturalAnalysisResult
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.model.OpenAIVisionRequest
import com.william.yachay_hco.model.OpenAIVisionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenAIService {
    @POST("chat/completions")
    suspend fun analyzeImage(
        @Header("Authorization") authToken: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body request: OpenAIVisionRequest
    ): OpenAIVisionResponse
}

// Para el backend Django (opcional)
interface CulturalService {
    @POST("cultural/analyze")
    suspend fun analyzeCulturalItem(@Body request: CulturalAnalysisRequest): CulturalAnalysisResponse

    @GET("cultural/items")
    suspend fun getCulturalItems(@Query("category") category: String?): List<CulturalItem>

    @GET("cultural/items/{id}")
    suspend fun getCulturalItem(@Path("id") id: Int): CulturalItem

    @POST("cultural/items")
    suspend fun saveCulturalItem(@Body item: CulturalItem): CulturalItem
}

data class CulturalAnalysisRequest(
    val image: String, // Base64 encoded image
    val location: String = "Huánuco, Perú"
)

data class CulturalAnalysisResponse(
    val result: CulturalAnalysisResult,
    val success: Boolean,
    val message: String? = null
)