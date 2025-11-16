package com.william.yachay_hco.ui.screens.report_screen.partes

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Funciones auxiliares
fun getCategoryDisplay(category: String): String {
    return when (category) {
        "GASTRONOMIA" -> "Gastronomía"
        "PATRIMONIO_ARQUEOLOGICO" -> "Patrimonio Arqueológico"
        "FLORA_MEDICINAL" -> "Flora Medicinal"
        "LEYENDAS_Y_TRADICIONES" -> "Leyendas y Tradiciones"
        "FESTIVIDADES" -> "Festividades"
        "DANZA" -> "Danza"
        "MUSICA" -> "Música"
        "VESTIMENTA" -> "Vestimenta"
        "ARTE_POPULAR" -> "Arte Popular"
        "NATURALEZA_CULTURAL" -> "Naturaleza/Cultural"
        "OTRO" -> "Otro"
        else -> category
    }
}

fun formatDateReport(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}

fun validateForm(
    motivo: String,
    titulo: String,
    descripcion: String,
    contextoCultural: String,
    periodoHistorico: String,
    ubicacion: String,
    significado: String
): Boolean {
    return motivo.isNotBlank() &&
            titulo.isNotBlank() &&
            descripcion.isNotBlank() &&
            contextoCultural.isNotBlank() &&
            periodoHistorico.isNotBlank() &&
            ubicacion.isNotBlank() &&
            significado.isNotBlank()
}