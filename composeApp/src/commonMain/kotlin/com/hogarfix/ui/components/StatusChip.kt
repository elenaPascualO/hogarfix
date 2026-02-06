package com.hogarfix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Schedule
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
import com.hogarfix.domain.model.Status
import com.hogarfix.ui.theme.StatusColors

@Composable
fun StatusChip(
    status: Status,
    modifier: Modifier = Modifier
) {
    val (color, icon, label) = when (status) {
        Status.PENDING -> Triple(StatusColors.Pending, Icons.Outlined.Schedule, "Pendiente")
        Status.IN_PROGRESS -> Triple(StatusColors.InProgress, Icons.Outlined.PlayCircle, "En curso")
        Status.COMPLETED -> Triple(StatusColors.Completed, Icons.Outlined.CheckCircle, "Completado")
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

fun getStatusColor(status: Status): Color {
    return when (status) {
        Status.PENDING -> StatusColors.Pending
        Status.IN_PROGRESS -> StatusColors.InProgress
        Status.COMPLETED -> StatusColors.Completed
    }
}

fun getStatusIcon(status: Status): ImageVector {
    return when (status) {
        Status.PENDING -> Icons.Outlined.Schedule
        Status.IN_PROGRESS -> Icons.Outlined.PlayCircle
        Status.COMPLETED -> Icons.Outlined.CheckCircle
    }
}

fun getStatusLabel(status: Status): String {
    return when (status) {
        Status.PENDING -> "Pendiente"
        Status.IN_PROGRESS -> "En curso"
        Status.COMPLETED -> "Completado"
    }
}
