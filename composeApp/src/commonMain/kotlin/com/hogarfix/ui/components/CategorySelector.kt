package com.hogarfix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.ElectricalServices
import androidx.compose.material.icons.outlined.FormatPaint
import androidx.compose.material.icons.outlined.Handyman
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Plumbing
import androidx.compose.material.icons.outlined.Roofing
import androidx.compose.material.icons.outlined.Window
import androidx.compose.material.icons.outlined.Yard
import androidx.compose.material.icons.outlined.Carpenter
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hogarfix.domain.model.Category
import com.hogarfix.ui.theme.CategoryColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelector(
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Category.entries.forEach { category ->
            CategoryChip(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = getCategoryColor(category)
    val backgroundColor = if (isSelected) color else Color.Transparent
    val contentColor = if (isSelected) Color.White else color

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = getCategoryIcon(category),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = getCategoryLabel(category),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}

@Composable
fun CategoryIcon(
    category: Category,
    modifier: Modifier = Modifier,
    size: Int = 40
) {
    val color = getCategoryColor(category)

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = getCategoryIcon(category),
            contentDescription = getCategoryLabel(category),
            tint = color,
            modifier = Modifier.size((size * 0.5).dp)
        )
    }
}

fun getCategoryColor(category: Category): Color {
    return when (category) {
        Category.PLUMBING -> CategoryColors.Plumbing
        Category.ELECTRICAL -> CategoryColors.Electrical
        Category.APPLIANCES -> CategoryColors.Appliances
        Category.PAINTING -> CategoryColors.Painting
        Category.LOCKSMITH -> CategoryColors.Locksmith
        Category.HVAC -> CategoryColors.Hvac
        Category.CARPENTRY -> CategoryColors.Carpentry
        Category.GARDEN -> CategoryColors.Garden
        Category.CLEANING -> CategoryColors.Cleaning
        Category.ROOFING -> CategoryColors.Roofing
        Category.FLOORING -> CategoryColors.Flooring
        Category.WINDOWS_DOORS -> CategoryColors.WindowsDoors
        Category.OTHER -> CategoryColors.Other
    }
}

fun getCategoryLabel(category: Category): String {
    return when (category) {
        Category.PLUMBING -> "Fontaneria"
        Category.ELECTRICAL -> "Electricidad"
        Category.APPLIANCES -> "Electrodomesticos"
        Category.PAINTING -> "Pintura"
        Category.LOCKSMITH -> "Cerrajeria"
        Category.HVAC -> "Climatizacion"
        Category.CARPENTRY -> "Carpinteria"
        Category.GARDEN -> "Jardin"
        Category.CLEANING -> "Limpieza"
        Category.ROOFING -> "Tejado"
        Category.FLOORING -> "Suelos"
        Category.WINDOWS_DOORS -> "Ventanas/Puertas"
        Category.OTHER -> "Otros"
    }
}

// Public function - used by other components (FilterChips, etc.)
fun getCategoryIcon(category: Category): ImageVector {
    return when (category) {
        Category.PLUMBING -> Icons.Outlined.Plumbing
        Category.ELECTRICAL -> Icons.Outlined.ElectricalServices
        Category.APPLIANCES -> Icons.Outlined.Kitchen
        Category.PAINTING -> Icons.Outlined.FormatPaint
        Category.LOCKSMITH -> Icons.Outlined.Lock
        Category.HVAC -> Icons.Outlined.AcUnit
        Category.CARPENTRY -> Icons.Outlined.Carpenter
        Category.GARDEN -> Icons.Outlined.Yard
        Category.CLEANING -> Icons.Outlined.CleaningServices
        Category.ROOFING -> Icons.Outlined.Roofing
        Category.FLOORING -> Icons.Outlined.Layers
        Category.WINDOWS_DOORS -> Icons.Outlined.Window
        Category.OTHER -> Icons.Outlined.Handyman
    }
}
