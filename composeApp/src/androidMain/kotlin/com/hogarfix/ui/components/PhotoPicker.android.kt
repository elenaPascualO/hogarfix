package com.hogarfix.ui.components

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberPhotoPicker(
    onPhotoSelected: (PhotoPickerResult) -> Unit
): PhotoPickerLauncher {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val bytes = readBytesFromUri(context, it)
            bytes?.let { photoBytes ->
                onPhotoSelected(PhotoPickerResult(photoBytes))
            }
        }
    }

    return remember(launcher) {
        PhotoPickerLauncher(
            launch = {
                launcher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }
}

private fun readBytesFromUri(context: Context, uri: android.net.Uri): ByteArray? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
    } catch (e: Exception) {
        null
    }
}

actual class PhotoPickerLauncher(
    private val launch: () -> Unit
) {
    actual fun launch() = launch.invoke()
}
