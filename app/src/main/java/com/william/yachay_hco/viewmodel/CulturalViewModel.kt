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
import kotlinx.coroutines.flow.update
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
    private val _selectedCulturalItem = MutableStateFlow<CulturalItem?>(null)
    val selectedCulturalItem: StateFlow<CulturalItem?> = _selectedCulturalItem.asStateFlow()

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

            culturalRepository.analyzeCulturalImage(imageBase64).fold(
                onSuccess = { result ->
                    Log.d(TAG, "Análisis exitoso: ${result.titulo}")
                    _analysisResult.value = result
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        analysisComplete = true
                    )
                },
                onFailure = { error ->
                    Log.e(TAG, "Error en análisis: ${error.message}")
                    val message = when (error) {
                        is CulturalRepository.AnalysisError.RateLimitError -> error.message
                        is CulturalRepository.AnalysisError.NotCulturalError -> error.message
                        is CulturalRepository.AnalysisError.NetworkError -> error.message
                        is CulturalRepository.AnalysisError.InvalidImageError -> error.message
                        else -> "Error al analizar la imagen: ${error.message}"
                    }
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        error = message
                    )
                }
            )
        }
    }

    fun saveCulturalItem(item: CulturalItem) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)

            culturalRepository.saveCulturalItem(item).fold(
                onSuccess = { savedItem ->
                    Log.d(TAG, "Elemento cultural guardado exitosamente")
                    val updatedList = _culturalItems.value.toMutableList()
                    updatedList.add(0, savedItem)
                    _culturalItems.value = updatedList

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        itemSaved = true
                    )
                },
                onFailure = { error ->
                    Log.e(TAG, "Error guardando elemento cultural: ${error.message}")
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = "No se pudo guardar el elemento: ${error.message}"
                    )
                }
            )
        }
    }

    fun loadCulturalItems(category: CulturalCategory? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            culturalRepository.getCulturalItems(category).fold(
                onSuccess = { items ->
                    Log.d(TAG, "Cargados ${items.size} elementos culturales")
                    _culturalItems.value = items
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = { error ->
                    Log.e(TAG, "Error cargando elementos culturales: ${error.message}")
                    _culturalItems.value = emptyList()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se pudieron cargar los elementos culturales"
                    )
                }
            )
        }
    }

    // Para obtener los items descubiertos por el usuario
    fun loadUserCulturalItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = culturalRepository.getUserCulturalItems()

            result
                .onSuccess { list ->
                    _culturalItems.value = list
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
        }
    }

    fun loadCulturalItemDetail(itemId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val item = culturalRepository.getCulturalItemDetail(itemId)
                _selectedCulturalItem.value = item
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
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