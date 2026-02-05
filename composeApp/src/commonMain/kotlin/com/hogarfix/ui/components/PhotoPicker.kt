package com.hogarfix.ui.components

import androidx.compose.runtime.Composable

data class PhotoPickerResult(
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as PhotoPickerResult
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int = bytes.contentHashCode()
}

@Composable
expect fun rememberPhotoPicker(
    onPhotoSelected: (PhotoPickerResult) -> Unit
): PhotoPickerLauncher

expect class PhotoPickerLauncher {
    fun launch()
}
