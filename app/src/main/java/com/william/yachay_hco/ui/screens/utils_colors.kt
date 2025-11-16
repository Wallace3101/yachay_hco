package com.william.yachay_hco.ui.screens

import androidx.compose.ui.graphics.Color
import com.william.yachay_hco.model.CulturalCategory

fun getCategoryColor(category: CulturalCategory): Color {
    return when (category) {
        CulturalCategory.GASTRONOMIA -> Color(0xFFFF7043)              // Naranja cálido
        CulturalCategory.PATRIMONIO_ARQUEOLOGICO -> Color(0xFF8D6E63)  // Marrón piedra
        CulturalCategory.FLORA_MEDICINAL -> Color(0xFF66BB6A)          // Verde medicinal
        CulturalCategory.LEYENDAS_Y_TRADICIONES -> Color(0xFF42A5F5)   // Azul narrativo
        CulturalCategory.FESTIVIDADES -> Color(0xFFFFC107)             // Amarillo festivo
        CulturalCategory.DANZA -> Color(0xFFE91E63)                    // Rosa vibrante
        CulturalCategory.MUSICA -> Color(0xFFAB47BC)                   // Morado melódico
        CulturalCategory.VESTIMENTA -> Color(0xFF5C6BC0)               // Azul índigo (ropa)
        CulturalCategory.ARTE_POPULAR -> Color(0xFFFF8A65)             // Naranja artístico
        CulturalCategory.NATURALEZA_CULTURAL -> Color(0xFF26A69A)      // Verde azulado (naturaleza)
        CulturalCategory.OTRO -> Color(0xFF9E9E9E)
    }
}