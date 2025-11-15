package com.william.yachay_hco.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.model.User
import com.william.yachay_hco.repository.AuthRepository
import com.william.yachay_hco.repository.CulturalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val culturalRepository: CulturalRepository,
) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()

    private val _userStats = MutableStateFlow(UserStats())
    val userStats: StateFlow<UserStats> = _userStats.asStateFlow()

    private val _culturalItems = MutableStateFlow<List<CulturalItem>>(emptyList())
    val culturalItems: StateFlow<List<CulturalItem>> = _culturalItems.asStateFlow()

    init {
        refreshUser()
        loadUserCulturalItems()
    }

    fun refreshUser() {
        viewModelScope.launch {
            _userState.value = authRepository.getCurrentUser()
        }
    }

    private fun loadUserCulturalItems() {
        viewModelScope.launch {
            try {
                val result = culturalRepository.getUserCulturalItems()

                val items = result.getOrElse { throwable ->
                    Log.e("ProfileViewModel", "Error loading user cultural items", throwable)
                    emptyList()
                }

                _culturalItems.value = items
                calculateUserStats(items)

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading user cultural items", e)
                _culturalItems.value = emptyList()
            }
        }
    }

    private fun calculateUserStats(items: List<CulturalItem>) {
        val totalAnalysis = items.size

        // Calcular items de la última semana
        val oneWeekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        val weeklyAnalysis = items.count { item ->
            try {
                // Parsear la fecha created_at del item
                val itemDate = parseItemDate(item.created_at)
                itemDate >= oneWeekAgo
            } catch (e: Exception) {
                false
            }
        }

        // Por ahora, favoritos y compartidos pueden ser 0 o calcularlos si tienes esa info
        val favorites = 0  // TODO: Implementar si tienes esta funcionalidad
        val shared = 0     // TODO: Implementar si tienes esta funcionalidad

        _userStats.value = UserStats(
            totalAnalysis = totalAnalysis,
            weeklyAnalysis = weeklyAnalysis,
            favorites = favorites,
            shared = shared
        )

        Log.d("ProfileViewModel", "Stats calculated - Total: $totalAnalysis, Weekly: $weeklyAnalysis")
    }

    private fun parseItemDate(dateString: String?): Long {
        if (dateString == null) return 0L

        return try {
            // Formato: "2024-11-13T12:30:45.123Z"
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
            sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
            sdf.parse(dateString)?.time ?: 0L
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Error parsing date: $dateString", e)
            0L
        }
    }
}

data class UserStats(
    val totalAnalysis: Int = 0,
    val favorites: Int = 0,
    val shared: Int = 0,
    val weeklyAnalysis: Int = 0
)