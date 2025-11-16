package com.william.yachay_hco.ui.screens.cultural_detail_screen.partes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.william.yachay_hco.model.CulturalItem
import com.william.yachay_hco.ui.theme.GreenYachay
import com.william.yachay_hco.ui.theme.RedYachay
import com.william.yachay_hco.ui.theme.YellowYachay

@Composable
fun ModernHeaderSection(item: CulturalItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.titulo,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            ModernConfidenceBadge(confidence = item.confianza)
        }
    }
}

@Composable
fun ModernConfidenceBadge(confidence: Float) {
    val (bgColor, textColor) = when {
        confidence >= 0.8f -> GreenYachay.copy(alpha = 0.15f) to GreenYachay
        confidence >= 0.6f -> YellowYachay.copy(alpha = 0.15f) to YellowYachay
        else -> RedYachay.copy(alpha = 0.15f) to RedYachay
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "${(confidence * 100).toInt()}%",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}