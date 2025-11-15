package com.william.yachay_hco.network

import retrofit2.http.POST
import retrofit2.http.Body

interface ProfileService {
    @POST("profile/save")
    suspend fun saveProfile(@Body profileData: Map<String, String?>): String
}