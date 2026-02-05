package com.hogarfix.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationSelectionOrdered
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberPhotoPicker(
    onPhotoSelected: (PhotoPickerResult) -> Unit
): PhotoPickerLauncher {
    return remember {
        PhotoPickerLauncher(onPhotoSelected)
    }
}

actual class PhotoPickerLauncher(
    private val onPhotoSelected: (PhotoPickerResult) -> Unit
) {
    actual fun launch() {
        val configuration = PHPickerConfiguration().apply {
            selectionLimit = 1
            filter = PHPickerFilter.imagesFilter
            selection = PHPickerConfigurationSelectionOrdered
        }

        val picker = PHPickerViewController(configuration = configuration)
        val delegate = PhotoPickerDelegate(onPhotoSelected)
        picker.delegate = delegate

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            picker,
            animated = true,
            completion = null
        )
    }
}

private class PhotoPickerDelegate(
    private val onPhotoSelected: (PhotoPickerResult) -> Unit
) : NSObject(), PHPickerViewControllerDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
        picker.dismissViewControllerAnimated(true, completion = null)

        val result = didFinishPicking.firstOrNull() as? PHPickerResult ?: return

        result.itemProvider.loadDataRepresentationForTypeIdentifier(
            typeIdentifier = "public.image"
        ) { data, error ->
            if (error != null || data == null) return@loadDataRepresentationForTypeIdentifier

            val bytes = nsDataToByteArray(data)
            if (bytes != null) {
                onPhotoSelected(PhotoPickerResult(bytes))
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun nsDataToByteArray(data: NSData): ByteArray? {
        val length = data.length.toInt()
        if (length == 0) return null

        val bytes = ByteArray(length)
        bytes.usePinned { pinned ->
            memcpy(pinned.addressOf(0), data.bytes, data.length)
        }
        return bytes
    }
}
