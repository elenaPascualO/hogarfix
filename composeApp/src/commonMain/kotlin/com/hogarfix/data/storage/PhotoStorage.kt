package com.hogarfix.data.storage

interface PhotoStorage {
    suspend fun savePhoto(entityType: String, entityId: Long, photoBytes: ByteArray): String
    suspend fun deletePhoto(photoUri: String)
    suspend fun getPhotoBytes(photoUri: String): ByteArray?
}

expect fun createPhotoStorage(): PhotoStorage
