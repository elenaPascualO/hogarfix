package com.hogarfix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hogarfix.domain.model.Reminder
import com.hogarfix.ui.theme.ReminderColors
import com.hogarfix.util.currentDate
import kotlinx.datetime.daysUntil
import kotlin.math.abs

enum class ReminderUrgency {
    OVERDUE, UPCOMING, ON_TRACK
}

fun getReminderUrgency(reminder: Reminder): ReminderUrgency {
    val today = currentDate()
    val daysUntilDue = today.daysUntil(reminder.nextDueDate)
    return when {
        daysUntilDue < 0 -> ReminderUrgency.OVERDUE
        daysUntilDue <= 7 -> ReminderUrgency.UPCOMING
        else -> ReminderUrgency.ON_TRACK
    }
}

fun getUrgencyColor(urgency: ReminderUrgency): Color {
    return when (urgency) {
        ReminderUrgency.OVERDUE -> ReminderColors.Overdue
        ReminderUrgency.UPCOMING -> ReminderColors.Soon
        ReminderUrgency.ON_TRACK -> ReminderColors.OnTrack
    }
}

@Composable
fun ReminderCard(
    reminder: Reminder,
    onClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(reminder.category)
    val urgency = getReminderUrgency(reminder)
    val urgencyColor = getUrgencyColor(urgency)
    val today = currentDate()
    val daysUntilDue = today.daysUntil(reminder.nextDueDate)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Accent bar izquierda (color categoria)
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(90.dp)
                    .background(categoryColor)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Urgency dot
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(urgencyColor)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = reminder.title,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = when {
                            daysUntilDue < 0 -> "Vencido hace ${abs(daysUntilDue)} dia${if (abs(daysUntilDue) != 1) "s" else ""}"
                            daysUntilDue == 0 -> "Vence hoy"
                            else -> "En $daysUntilDue dia${if (daysUntilDue != 1) "s" else ""}"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = urgencyColor
                    )

                    if (!reminder.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = reminder.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Complete button
                IconButton(onClick = onComplete) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "Completar",
                        tint = ReminderColors.OnTrack,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
