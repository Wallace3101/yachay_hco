package com.william.yachay_hco.network

import com.william.yachay_hco.model.ApiResponse
import com.william.yachay_hco.model.CreateReportRequest
import com.william.yachay_hco.model.CulturalAnalysisRequest
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.model.CulturalItemResponse
import com.william.yachay_hco.model.CulturalItemsResponse
import com.william.yachay_hco.model.ReportResponse
import com.william.yachay_hco.model.ReportsResponse
import com.william.yachay_hco.model.ReviewReportRequest
import com.william.yachay_hco.model.ReviewReportResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Para el backend Django (opcional)
interface CulturalService {

    // ========== ENDPOINTS DE CULTURAL ITEMS ==========

    @POST("api/cultural/analyze/")
    suspend fun analyzeImage(
        @Header("Authorization") authToken: String,
        @Body request: CulturalAnalysisRequest
    ): ApiResponse<CulturalItem>

    @GET("api/cultural/items")
    suspend fun getCulturalItems(
        @Header("Authorization") authToken: String,
        @Query("category") category: String?
    ): CulturalItemsResponse

    @GET("api/cultural/items/{id}")
    suspend fun getCulturalItemDetail(
        @Header("Authorization") authToken: String,
        @Path("id") id: Int
    ): CulturalItemResponse

    @POST("api/cultural/items/create")
    suspend fun saveCulturalItem(
        @Header("Authorization") authToken: String,
        @Body item: CulturalItem
    ): CulturalItemResponse

    @GET("api/cultural/items/me")
    suspend fun getMyCulturalItems(
        @Header("Authorization") authToken: String
    ): CulturalItemsResponse

    // ========== ENDPOINTS DE REPORTES ==========

    @POST("api/cultural/reports/create")
    suspend fun createReport(
        @Header("Authorization") authToken: String,
        @Body request: CreateReportRequest
    ): ReportResponse

    @GET("api/cultural/reports/my-reports")
    suspend fun getUserReports(
        @Header("Authorization") authToken: String
    ): ReportsResponse

    @GET("api/cultural/reports/all")
    suspend fun getAllReports(
        @Header("Authorization") authToken: String,
        @Query("status") status: String? = null
    ): ReportsResponse

    @GET("api/cultural/reports/{id}")
    suspend fun getReportDetail(
        @Header("Authorization") authToken: String,
        @Path("id") reportId: Int
    ): ReportResponse

    @POST("api/cultural/reports/{id}/review")
    suspend fun reviewReport(
        @Header("Authorization") authToken: String,
        @Path("id") reportId: Int,
        @Body request: ReviewReportRequest
    ): ReviewReportResponse
}