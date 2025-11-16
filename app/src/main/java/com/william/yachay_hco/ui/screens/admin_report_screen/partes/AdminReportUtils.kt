package com.william.yachay_hco.ui.screens.admin_report_screen.partes

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDateReportAdmin(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}

fun getCategoryDisplayAdmin(category: String): String {
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