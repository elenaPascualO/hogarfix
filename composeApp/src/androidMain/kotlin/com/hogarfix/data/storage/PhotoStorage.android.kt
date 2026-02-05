package com.hogarfix.data.storage

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

private var appContext: Context? = null

fun initPhotoStorageContext(context: Context) {
    appContext = context.applicationContext
}

actual fun createPhotoStorage(): PhotoStorage = AndroidPhotoStorage()

class AndroidPhotoStorage : PhotoStorage {

    private val photosDir: File
        get() {
            val context = appContext
                ?: throw IllegalStateException("PhotoStorage context not initialized. Call initPhotoStorageContext first.")
            return File(context.filesDir, "photos").also { it.mkdirs() }
        }

    override suspend fun savePhoto(interventionId: Long, photoBytes: ByteArray): String =
        withContext(Dispatchers.IO) {
            val fileName = "intervention_${interventionId}_${UUID.randomUUID()}.jpg"
            val file = File(photosDir, fileName)
            file.writeBytes(photoBytes)
            file.absolutePath
        }

    override suspend fun deletePhoto(photoUri: String): Unit = withContext(Dispatchers.IO) {
        val file = File(photoUri)
        if (file.exists()) {
            file.delete()
        }
    }

    override suspend fun getPhotoBytes(photoUri: String): ByteArray? =
        withContext(Dispatchers.IO) {
            val file = File(photoUri)
            if (file.exists()) file.readBytes() else null
        }
}
