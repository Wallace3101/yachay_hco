package com.william.yachay_hco.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.william.yachay_hco.model.CulturalCategory
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.repository.CulturalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CulturalViewModel @Inject constructor(
    private val culturalRepository: CulturalRepository
) : ViewModel() {

    companion object {
        private const val TAG = "CulturalViewModel"
    }

    private val _uiState = MutableStateFlow(CulturalUiState())
    val uiState: StateFlow<CulturalUiState> = _uiState.asStateFlow()

    private val _culturalItems = MutableStateFlow<List<CulturalItem>>(emptyList())
    val culturalItems: StateFlow<List<CulturalItem>> = _culturalItems.asStateFlow()

    private val _analysisResult = MutableStateFlow<CulturalItem?>(null)
    val analysisResult: StateFlow<CulturalItem?> = _analysisResult.asStateFlow()

    init {
        loadCulturalItems()
    }

    fun analyzeImage(imageBase64: String) {
        Log.d(TAG, "Iniciando análisis de imagen")

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAnalyzing = true,
                error = null,
                analysisComplete = false
            )

            try {
                Log.d(TAG, "Enviando imagen al repositorio para análisis")
                val result = culturalRepository.analyzeCulturalImage(imageBase64)

                if (result != null) {
                    Log.d(TAG, "Análisis exitoso: ${result.titulo}")
                    _analysisResult.value = result
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        analysisComplete = true
                    )
                } else {
                    Log.e(TAG, "El análisis retornó null")
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        error = "No se pudo analizar la imagen. Inténtalo nuevamente."
                    )
                }

            } catch (e: IllegalStateException) {
                // Errores de validación (límite de tiempo, etc.)
                Log.w(TAG, "Error de validación: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    error = e.message ?: "Error de validación"
                )
            } catch (e: IllegalArgumentException) {
                // Errores de argumentos inválidos
                Log.w(TAG, "Argumento inválido: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    error = e.message ?: "Imagen inválida"
                )
            } catch (e: Exception) {
                // Otros errores
                Log.e(TAG, "Error inesperado: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    error = e.message ?: "Error inesperado al analizar la imagen"
                )
            }
        }
    }

    fun saveCulturalItem(item: CulturalItem) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)

            try {
                Log.d(TAG, "Guardando elemento cultural: ${item.titulo}")
                val savedItem = culturalRepository.saveCulturalItem(item)

                // Actualizar la lista local
                val updatedList = _culturalItems.value.toMutableList()
                updatedList.add(0, savedItem)
                _culturalItems.value = updatedList

                Log.d(TAG, "Elemento cultural guardado exitosamente")
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    itemSaved = true
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error guardando elemento cultural: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Error al guardar: ${e.message}"
                )
            }
        }
    }

    fun loadCulturalItems(category: CulturalCategory? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                Log.d(TAG, "Cargando elementos culturales${if (category != null) " de categoría $category" else ""}")
                val items = culturalRepository.getCulturalItems(category)
                _culturalItems.value = items
                _uiState.value = _uiState.value.copy(isLoading = false)
                Log.d(TAG, "Cargados ${items.size} elementos culturales")
            } catch (e: Exception) {
                Log.e(TAG, "Error cargando elementos culturales: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar elementos: ${e.message}"
                )
            }
        }
    }

    fun filterByCategory(category: CulturalCategory?) {
        loadCulturalItems(category)
    }

    fun clearAnalysisResult() {
        Log.d(TAG, "Limpiando resultado de análisis")
        _analysisResult.value = null
        _uiState.value = _uiState.value.copy(analysisComplete = false)
    }

    fun clearError() {
        Log.d(TAG, "Limpiando error")
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetSaveState() {
        Log.d(TAG, "Reseteando estado de guardado")
        _uiState.value = _uiState.value.copy(itemSaved = false)
    }
}

data class CulturalUiState(
    val isLoading: Boolean = false,
    val isAnalyzing: Boolean = false,
    val isSaving: Boolean = false,
    val analysisComplete: Boolean = false,
    val itemSaved: Boolean = false,
    val error: String? = null
)