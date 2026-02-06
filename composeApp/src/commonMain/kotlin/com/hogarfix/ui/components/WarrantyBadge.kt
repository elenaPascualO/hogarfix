package com.hogarfix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hogarfix.util.currentDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

@Composable
fun WarrantyBadge(
    warrantyEndDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val today = currentDate()
    val daysRemaining = today.daysUntil(warrantyEndDate)

    val (backgroundColor, textColor, text) = when {
        daysRemaining < 0 -> Triple(
            Color(0xFFFFEBEE), // Light red background
            Color(0xFFD32F2F), // Red text
            "Vencida"
        )
        daysRemaining <= 30 -> Triple(
            Color(0xFFFFEBEE),
            Color(0xFFD32F2F),
            "$daysRemaining dias"
        )
        daysRemaining <= 90 -> Triple(
            Color(0xFFFFF8E1), // Light amber background
            Color(0xFFF9A825), // Amber text
            "$daysRemaining dias"
        )
        else -> Triple(
            Color(0xFFE8F5E9), // Light green background
            Color(0xFF388E3C), // Green text
            "$daysRemaining dias"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
}
