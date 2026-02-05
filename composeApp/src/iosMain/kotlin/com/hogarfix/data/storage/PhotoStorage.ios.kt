package com.hogarfix.data.storage

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.writeToFile
import platform.posix.memcpy

actual fun createPhotoStorage(): PhotoStorage = IosPhotoStorage()

@OptIn(ExperimentalForeignApi::class)
class IosPhotoStorage : PhotoStorage {

    private val photosDir: String
        get() {
            val paths = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            )
            val documentsDir = paths.firstOrNull() as? String ?: ""
            val photosPath = "$documentsDir/photos"
            NSFileManager.defaultManager.createDirectoryAtPath(
                photosPath,
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
            return photosPath
        }

    @OptIn(BetaInteropApi::class)
    override suspend fun savePhoto(interventionId: Long, photoBytes: ByteArray): String =
        withContext(Dispatchers.IO) {
            val fileName = "intervention_${interventionId}_${NSUUID().UUIDString}.jpg"
            val filePath = "$photosDir/$fileName"

            val nsData = photoBytes.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = photoBytes.size.toULong())
            }
            nsData.writeToFile(filePath, atomically = true)
            filePath
        }

    override suspend fun deletePhoto(photoUri: String): Unit = withContext(Dispatchers.IO) {
        NSFileManager.defaultManager.removeItemAtPath(photoUri, error = null)
    }

    override suspend fun getPhotoBytes(photoUri: String): ByteArray? =
        withContext(Dispatchers.IO) {
            val nsData = NSData.dataWithContentsOfFile(photoUri) ?: return@withContext null
            val bytes = ByteArray(nsData.length.toInt())
            bytes.usePinned { pinned ->
                memcpy(pinned.addressOf(0), nsData.bytes, nsData.length)
            }
            bytes
        }
}
