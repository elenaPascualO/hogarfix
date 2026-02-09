package com.hogarfix.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Int?,
    onRatingChanged: ((Int?) -> Unit)? = null,
    modifier: Modifier = Modifier,
    starSize: Dp = 24.dp,
    starColor: Color = Color(0xFFFFC107)
) {
    val interactive = onRatingChanged != null

    Row(modifier = modifier) {
        for (star in 1..5) {
            val filled = rating != null && star <= rating
            Icon(
                imageVector = if (filled) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = "Estrella $star",
                tint = if (filled) starColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier
                    .size(starSize)
                    .then(
                        if (interactive) {
                            Modifier.clickable {
                                // Tap same star to clear rating
                                if (rating == star) {
                                    onRatingChanged!!(null)
                                } else {
                                    onRatingChanged!!(star)
                                }
                            }
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}
