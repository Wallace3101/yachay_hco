package com.william.yachay_hco.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.william.yachay_hco.model.User
import com.william.yachay_hco.model.CulturalItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("yachay_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    // Constantes
    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_DATA = "user_data"
        private const val KEY_CULTURAL_ITEMS = "cultural_items"
        private const val KEY_OPENAI_API_KEY = "openai_api_key"
        private const val KEY_LAST_ANALYSIS_TIME = "last_analysis_time"
    }

    // Métodos de autenticación existentes
    fun saveAuthToken(token: String) {
        sharedPreferences.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit()
            .putString(KEY_USER_DATA, userJson)
            .apply()
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER_DATA, null)
        return if (userJson != null) {
            try {
                gson.fromJson(userJson, User::class.java)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    fun clearAuthData() {
        sharedPreferences.edit()
            .remove(KEY_AUTH_TOKEN)
            .remove(KEY_USER_DATA)
            .apply()
    }

    // Nuevos métodos para elementos culturales
    fun saveCulturalItems(items: List<CulturalItem>) {
        val itemsJson = gson.toJson(items)
        sharedPreferences.edit()
            .putString(KEY_CULTURAL_ITEMS, itemsJson)
            .apply()
    }

    fun getCulturalItems(): List<CulturalItem> {
        val itemsJson = sharedPreferences.getString(KEY_CULTURAL_ITEMS, null)
        return if (itemsJson != null) {
            try {
                val itemsArray = gson.fromJson(itemsJson, Array<CulturalItem>::class.java)
                itemsArray.toList()
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()
    }

    fun addCulturalItem(item: CulturalItem) {
        val currentItems = getCulturalItems().toMutableList()
        currentItems.add(0, item) // Agregar al inicio
        saveCulturalItems(currentItems)
    }

    fun removeCulturalItem(itemId: Int) {
        val currentItems = getCulturalItems().toMutableList()
        currentItems.removeAll { it.id == itemId }
        saveCulturalItems(currentItems)
    }

    // Configuraciones de la API de OpenAI
    fun saveOpenAIApiKey(apiKey: String) {
        sharedPreferences.edit()
            .putString(KEY_OPENAI_API_KEY, apiKey)
            .apply()
    }

    fun getOpenAIApiKey(): String? {
        return sharedPreferences.getString(KEY_OPENAI_API_KEY, null)
    }

    // Control de frecuencia de análisis
    fun saveLastAnalysisTime(timestamp: Long) {
        sharedPreferences.edit()
            .putLong(KEY_LAST_ANALYSIS_TIME, timestamp)
            .apply()
    }

    fun getLastAnalysisTime(): Long {
        return sharedPreferences.getLong(KEY_LAST_ANALYSIS_TIME, 0)
    }

    fun canPerformAnalysis(): Boolean {
        val lastAnalysis = getLastAnalysisTime()
        val currentTime = System.currentTimeMillis()
        val minimumInterval = 30 * 1000 // 30 segundos entre análisis

        return (currentTime - lastAnalysis) > minimumInterval
    }

    // Configuraciones de la aplicación
    fun setFirstLaunch(isFirst: Boolean) {
        sharedPreferences.edit()
            .putBoolean("is_first_launch", isFirst)
            .apply()
    }

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean("is_first_launch", true)
    }

    // Estadísticas de uso
    fun incrementAnalysisCount() {
        val currentCount = sharedPreferences.getInt("analysis_count", 0)
        sharedPreferences.edit()
            .putInt("analysis_count", currentCount + 1)
            .apply()
    }

    fun getAnalysisCount(): Int {
        return sharedPreferences.getInt("analysis_count", 0)
    }

    // Configuraciones de notificaciones y preferencias
    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean("notifications_enabled", enabled)
            .apply()
    }

    fun areNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean("notifications_enabled", true)
    }
}