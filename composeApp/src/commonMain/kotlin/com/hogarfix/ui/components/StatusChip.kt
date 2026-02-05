package com.hogarfix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hogarfix.domain.model.Status
import com.hogarfix.ui.theme.StatusColors

@Composable
fun StatusChip(
    status: Status,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, label) = when (status) {
        Status.PENDING -> StatusColors.Pending to "Pendiente"
        Status.IN_PROGRESS -> StatusColors.InProgress to "En curso"
        Status.COMPLETED -> StatusColors.Completed to "Completado"
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = Color.White,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
